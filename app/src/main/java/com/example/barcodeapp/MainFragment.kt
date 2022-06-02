package com.example.barcodeapp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.databinding.FragmentMainBinding
import com.example.barcodeapp.functions.Constants
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.functions.getFirst
import com.example.barcodeapp.functions.navOptions
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ViewModelFactory
import com.example.barcodeapp.worker.SyncWorker
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val TAG = "MainFragment"

    private lateinit var appDatabase: AppDatabase
    private lateinit var networkHelper: NetworkHelper
    private lateinit var repository: CodeRepository
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var product: ProductEntity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())
        repository = CodeRepository(appDatabase, ApiClient.webservice)
        categoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, networkHelper)
        )[CategoryViewModel::class.java]

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()

        val workRequest = PeriodicWorkRequest
            .Builder(SyncWorker::class.java, 30, TimeUnit.MINUTES)
            .setConstraints(constraints).build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)


        val barcodeReceiver = BarcodeScanningListener()
        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
        requireContext().registerReceiver(barcodeReceiver, intentFilter)

        binding.parameterBtn.setOnClickListener {
            findNavController().navigate(R.id.settingFragment, Bundle(), navOptions())
        }

        binding.tv.text = getFirst(requireActivity())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tv.text = getFirst(requireActivity())
    }


    inner class BarcodeScanningListener : BroadcastReceiver() {
        @SuppressLint("UnsafeProtectedBroadcastReceiver")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val scannedBarcode = p1?.getStringExtra("SCAN_BARCODE1")
            val scanStatus = p1?.getStringExtra("SCAN_STATE")
            var products = ArrayList<ProductEntity>()
            if ("ok" == scanStatus) {
                Log.d(TAG, "onReceive barcode: $scannedBarcode")
                Toast.makeText(p0, scannedBarcode, Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    repository.getDBProducts().catch {
                        Toast.makeText(p0, it.message, Toast.LENGTH_SHORT).show()
                    }.collect {
                        products = it as ArrayList<ProductEntity>
                    }
                    var isHave = true
                    var id = ""
                    break1@ for (it1 in products) {
                        Log.d(TAG, "onCreate1: $it1")
                        val barcodes = it1.barcodes
                        break2@ for (it2 in barcodes) {
                            if (it2 == scannedBarcode) {
                                Log.i(TAG, "onCreate: $it2")
                                Log.d(TAG, "onCreateT: $it2")
                                id = it1.id
                                product = ProductEntity(
                                    it1.id,
                                    it1.barcodes,
                                    it1.code,
                                    it1.measurement,
                                    it1.salesPrice,
                                    it1.name,
                                    it1.description,
                                    it1.imageUrl,
                                    it1.categoryId
                                )
                                isHave = false
                                break@break1
                            }
                        }
                    }
                    if (!isHave) {
                        if (id != "") {
                            val bundle = Bundle()
                            bundle.putSerializable("product", product)
                            Constants.productEntity = product
                            findNavController().navigate(
                                R.id.product_details_fragment,
                                bundle,
                                navOptions()
                            )
//
                            Log.d(TAG, "onCreate123: $id")
                        } else {
                            Log.d(TAG, "onCreate:BMY ")
                        }
                    } else {
                        Log.d(TAG, "onCreate: Bunday malumot yuq")
                        Toast.makeText(p0, "Malumot topilmadi", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.d(TAG, "onReceive: Scanner bulmadi")
                Toast.makeText(p0, "Barcodeni o'qishni imkoni bo'lmadi", Toast.LENGTH_SHORT).show()
            }
        }

    }
//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MainFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}