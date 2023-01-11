package com.example.barcodeapp

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
import com.example.barcodeapp.adapters.CategoryAdapter
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.databinding.FragmentHomeBinding
import com.example.barcodeapp.functions.Constants.categoryEntity
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.functions.navOptions
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.CategoryResource
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var categoryAdapter: CategoryAdapter
    private val binding get() = _binding!!

    private val TAG = "CategoryListFragment"
    private lateinit var appDatabase: AppDatabase
    private lateinit var networkHelper: NetworkHelper
    private lateinit var repository: CodeRepository
    private lateinit var list: ArrayList<CategoryEntity>
    private lateinit var product: ProductEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        list = ArrayList()
        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())
        repository = CodeRepository(appDatabase, ApiClient.webservice,requireContext())
        categoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, networkHelper,requireContext(),appDatabase)
        )[CategoryViewModel::class.java]

        lifecycleScope.launch(Dispatchers.Main) {
            categoryViewModel.getAllCategories().collect {
                when (it) {
                    is CategoryResource.Error -> {
                        binding.card.visibility = View.GONE
                        Log.d(TAG, "onCreateView: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Success -> {
                        binding.card.visibility = View.GONE
                        list.addAll(it.list)
                        categoryAdapter =
                            CategoryAdapter(it.list, object : CategoryAdapter.OnItemClick {
                                override fun onItemClick(category: CategoryEntity) {
                                    val bundle = Bundle()
                                    categoryEntity = category
                                    bundle.putSerializable("category", category)
                                    findNavController().navigate(
                                        R.id.listFragment,
                                        bundle,
                                        navOptions()
                                    )
                                }
                            })
                        binding.rv.adapter = categoryAdapter


                        Log.d(TAG, "onCreateView111: ${it.list}")
                    }
                    is CategoryResource.Loading -> {
                        binding.card.visibility = View.VISIBLE
                    }
                }
            }
        }


//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresCharging(true)
//            .build()
//
//        val workRequest = PeriodicWorkRequest
//            .Builder(SyncWorker::class.java, 30, TimeUnit.MINUTES)
//            .setConstraints(constraints).build()
//        WorkManager.getInstance(requireContext()).enqueue(workRequest)
//
//
//        val barcodeReceiver = BarcodeScanningListener()
//        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
//        requireContext().registerReceiver(barcodeReceiver, intentFilter)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    inner class BarcodeScanningListener : BroadcastReceiver() {
//        @SuppressLint("UnsafeProtectedBroadcastReceiver")
//        override fun onReceive(p0: Context?, p1: Intent?) {
//            val scannedBarcode = p1?.getStringExtra("SCAN_BARCODE1")
//            val scanStatus = p1?.getStringExtra("SCAN_STATE")
//            var products = ArrayList<ProductEntity>()
//            if ("ok" == scanStatus) {
//                Log.d(TAG, "onReceive barcode: $scannedBarcode")
//                Toast.makeText(p0, scannedBarcode, Toast.LENGTH_SHORT).show()
//                lifecycleScope.launch {
//                    repository.getDBProducts().catch {
//                        Toast.makeText(p0, it.message, Toast.LENGTH_SHORT).show()
//                    }.collect {
//                        products = it as ArrayList<ProductEntity>
//                    }
//                    var isHave = true
//                    var id = ""
//                    break1@ for (it1 in products) {
//                        Log.d(TAG, "onCreate1: $it1")
//                        val barcodes = it1.barcodes
//                        break2@ for (it2 in barcodes) {
//                            if (it2 == scannedBarcode) {
//                                Log.i(TAG, "onCreate: $it2")
//                                Log.d(TAG, "onCreateT: $it2")
//                                id = it1.id
//                                product = ProductEntity(
//                                    it1.id,
//                                    it1.barcodes,
//                                    it1.code,
//                                    it1.measurement,
//                                    it1.salesPrice,
//                                    it1.name,
//                                    it1.description,
//                                    it1.imageUrl,
//                                    it1.categoryId
//                                )
//                                isHave = false
//                                break@break1
//                            }
//                        }
//                    }
//                    if (!isHave) {
//                        if (id != "") {
//                            val bundle = Bundle()
//                            bundle.putSerializable("product", product)
//                            Constants.productEntity = product
//                            findNavController().navigate(
//                                R.id.product_details_fragment,
//                                bundle,
//                                navOptions()
//                            )
////
//                            Log.d(TAG, "onCreate123: $id")
//                        } else {
//                            Log.d(TAG, "onCreate:BMY ")
//                        }
//                    } else {
//                        Log.d(TAG, "onCreate: Bunday malumot yuq")
//                        Toast.makeText(p0, "Malumot topilmadi", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else {
//                Log.d(TAG, "onReceive: Scanner bulmadi")
//                Toast.makeText(p0, "Barcodeni o'qishni imkoni bo'lmadi", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//                            requireContext().unregisterReceiver(this@BarcodeScanningListener)
//                            Log.d(TAG, "onCreate123: $id")
//                        } else {
//                            Log.d(TAG, "onCreate:BMY ")
//                        }
//                    } else {
//                        Log.d(TAG, "onCreate: Bunday malumot yuq")
//                        Toast.makeText(p0, "Malumot topilmadi", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else {
//                Log.d(TAG, "onReceive: Scanner bulmadi")
//                Toast.makeText(p0, "Barcodeni o'qishni imkoni bo'lmadi", Toast.LENGTH_SHORT).show()
//            }
//        }

//    }

    private fun getProducts() {
//        lifecycleScope.launch { categoryViewModel.getProducts().collect {} }
    }
//    }
}

