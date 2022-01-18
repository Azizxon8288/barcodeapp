package com.example.barcodeapp

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.*
//import com.example.barcodeapp.adapters.CategoryAdapter
import com.example.barcodeapp.broadcast.BarcodeReceiver
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.data.service.Webservice
import com.example.barcodeapp.databinding.FragmentHomeBinding
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.UsersResource
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ViewModelFactory
import com.example.barcodeapp.worker.UploadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    @SuppressLint("RestrictedApi", "VisibleForTests")
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

        lifecycleScope.launch /**(Dispatchers.Main)**/  {
            categoryViewModel.getUsers().collect {
                when (it) {
                    is UsersResource.Error -> {

                    }
                    is UsersResource.Success -> {
                        it.list
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


        val barcodeReceiver = BarcodeReceiver()
        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT");
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

}