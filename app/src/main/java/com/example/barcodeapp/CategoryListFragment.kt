package com.example.barcodeapp

//import com.example.barcodeapp.adapters.CategoryAdapter
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.barcodeapp.broadcast.BarcodeReceiver
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.databinding.FragmentHomeBinding
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.SearchResource
import com.example.barcodeapp.resource.UsersResource
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ViewModelFactory
import com.example.barcodeapp.worker.UploadWorker
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentHomeBinding? = null
    private lateinit var categoryViewModel: CategoryViewModel

    //    private lateinit var categoryAdapter: CategoryAdapter
    private val binding get() = _binding!!

    private val TAG = "CategoryListFragment"
    private lateinit var appDatabase: AppDatabase
    private lateinit var networkHelper: NetworkHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())


        categoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(CodeRepository(appDatabase, ApiClient.webservice), networkHelper)
        )[CategoryViewModel::class.java]

        lifecycleScope.launch {
            /**(Dispatchers.Main)**/
            categoryViewModel.getUsers().collect {
                when (it) {
                    is UsersResource.Error -> {

                    }
                    is UsersResource.Success -> {
                        Log.d(TAG, "onCreateView: ${it.list}")
                    }
                    is UsersResource.Loading -> {

                    }
                }
            }
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()

        val workRequest = PeriodicWorkRequest
            .Builder(UploadWorker::class.java, 30, TimeUnit.MINUTES)
            .setConstraints(constraints).build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)


//        val uploadWorkRequest: WorkRequest =
//            OneTimeWorkRequestBuilder<UploadWorker>()
//                .setScheduleRequestedAt(15, TimeUnit.MINUTES)
//                .build()
//        WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest)


//        categoryAdapter = CategoryAdapter(loadData(), object : CategoryAdapter.OnItemClick {
//            override fun onItemClick(category: Category) {
//                val bundle = Bundle()
//                bundle.putSerializable("category", category)
//                findNavController().navigate(R.id.listFragment, bundle, navOptions())
//            }
//
//        })


        val barcodeReceiver = BarCode()
        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
        requireContext().registerReceiver(barcodeReceiver, intentFilter)

        binding.apply {
//            rv.adapter = categoryAdapter
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //    private fun loadData(): List<Category> {
//        val list = ArrayList<Category>()
//        list.add(Category("Oziq-Ovqatlar", R.drawable.food_svg))
//        list.add(Category("Mevalar", R.drawable.fruits_svg))
//        list.add(Category("Sabzavotlar", R.drawable.vegatable_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Oziq-Ovqatlar", R.drawable.food_svg))
//        list.add(Category("Mevalar", R.drawable.fruits_svg))
//        list.add(Category("Sabzavotlar", R.drawable.vegatable_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Mevalar", R.drawable.fruits_svg))
//        list.add(Category("Oziq-Ovqatlar", R.drawable.food_svg))
//        list.add(Category("Sabzavotlar", R.drawable.vegatable_svg))
//        return list
//    }
    inner class BarCode : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            val scannedBarcode = p1?.getStringExtra("SCAN_BARCODE1")
            val scanStatus = p1?.getStringExtra("SCAN_STATE")

            if ("ok" == scanStatus) {
                Log.d(TAG, "onReceive: $scannedBarcode")
                Toast.makeText(p0, scannedBarcode, Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    categoryViewModel.searchByBarCode(scannedBarcode).collect {
                        when (it) {
                            is SearchResource.Error -> {
                                Toast.makeText(p0, it.message, Toast.LENGTH_SHORT).show()
                            }
                            is SearchResource.Success -> {
                                Log.d(TAG, "onReceive: ${it.productEntity}")
                                Toast.makeText(p0, "${it.productEntity}", Toast.LENGTH_SHORT).show()
                                val action = Intent(context, ProductDetailsFragment::class.java)
                                action.putExtra("search", it.productEntity)
                                p0?.startActivity(action)
                            }
                            is SearchResource.Loading -> {

                            }
                        }
                    }
                }

            } else {
                Log.d(TAG, "onReceive: Scanner bulmadi")
                Toast.makeText(context, "Scanner bulmadi", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

