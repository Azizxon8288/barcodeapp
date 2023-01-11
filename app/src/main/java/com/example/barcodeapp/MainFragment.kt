package com.example.barcodeapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.barcodeapp.adapters.PagerAdapter
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.databinding.FragmentMainBinding
import com.example.barcodeapp.functions.Constants
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.functions.getFirst
import com.example.barcodeapp.functions.navOptions
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ProductState
import com.example.barcodeapp.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    private var count = 0

    lateinit var unexpectedErrorTv: TextView
    lateinit var notFoundErrorTv: TextView
    lateinit var centerTv: TextView

    lateinit var pagerAdapter: PagerAdapter
    lateinit var progress: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())
        pagerAdapter = PagerAdapter()
        binding.rv.adapter = pagerAdapter
        repository = CodeRepository(appDatabase, ApiClient.webservice, requireContext())
        categoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, networkHelper, requireContext(), appDatabase)
        )[CategoryViewModel::class.java]

//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresCharging(true)
//            .build()

//        val workRequest = PeriodicWorkRequest
//            .Builder(SyncWorker::class.java, 30, TimeUnit.MINUTES)
//            .setConstraints(constraints).build()
//        WorkManager.getInstance(requireContext()).enqueue(workRequest)


        val barcodeReceiver = BarcodeScanningListener()
        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
        requireContext().registerReceiver(barcodeReceiver, intentFilter)

        unexpectedErrorTv = binding.tvUnexpectedError
        notFoundErrorTv = binding.tvNotFoundError
        centerTv = binding.tv

        binding.parameterBtn.setOnClickListener {
            findNavController().navigate(R.id.settingFragment, Bundle(), navOptions())
        }

        progress = ProgressDialog(requireContext())
        progress.setMessage("Maxsulotlar yuklanmoqda")

        lifecycleScope.launch {
            categoryViewModel.getProductsChanges(1)
        }

        lifecycleScope.launch {
            categoryViewModel.getProduct().collectLatest {
                when (it) {
                    is ProductState.SuccessProduct -> {
                    }
                    is ProductState.ErrorProduct -> {
                    }
                    is ProductState.IsLoading -> {
                    }
                    is ProductState.Init -> {
//                        progress.show()
//                        Log.e("show","show Init")

                    }
                    is ProductState.ShowToast -> {
                    }
                }
                Log.e("succes", it.toString())
            }
        }
        lifecycleScope.launch {
            categoryViewModel._mState.collectLatest {
                when (it) {
                    is ProductState.Init -> {
                        progress.show()
                        Log.e("show", "show Init")
                    }
                    is ProductState.IsLoading -> {
                        if (!it.isLoading) {
                            Log.e("hide", "hide isLoading")
                            progress.hide()
                        }
                    }
                    else -> {
                        Log.e("1", "2error mainFragment")
                    }
                }
            }
        }

        binding.tv.text = getFirst(requireActivity())
        return binding.root
    }

//    var isHave = false

    override fun onResume() {
        super.onResume()
//        if (!isHave) {
//            isHave = true
//            progress.show()
//        } else {
//            progress.dismiss()
//        }
        binding.tv.text = getFirst(requireActivity())
    }


    inner class BarcodeScanningListener : BroadcastReceiver() {
        @SuppressLint("UnsafeProtectedBroadcastReceiver")
        override fun onReceive(p0: Context?, p1: Intent?) {
            var barcode = ""
            val scannedBarcode = p1?.getStringExtra("SCAN_BARCODE1")
            val scannedBarcodeSecond = p1?.getStringExtra("SCAN_BARCODE2")
            val scanStatus = p1?.getStringExtra("SCAN_STATE")
            if ("ok" == scanStatus) {
                barcode = if ((scannedBarcode?.length ?: 0) > 0) {
                    scannedBarcode ?: ""
                } else {
                    scannedBarcodeSecond ?: ""
                }
                Log.d(TAG, "onReceive barcode: $scannedBarcode")
                Log.d(TAG, "onReceive barcode: $scannedBarcodeSecond")
                lifecycleScope.launch {
                    repository.searchByBarCode(barcode)
                        .catch {
//                            centerTv.visibility = View.GONE
//                            unexpectedErrorTv.visibility = View.VISIBLE
//                            notFoundErrorTv.visibility = View.GONE
//                            Handler(Looper.getMainLooper()).postDelayed({
//                                if (_binding != null) {
//                                    centerTv.visibility = View.VISIBLE
//                                    unexpectedErrorTv.visibility = View.GONE
//                                    notFoundErrorTv.visibility = View.GONE
//                                } else {
//                                    Log.e("ABC", "Error")
//                                }
//                            }, 2000)
                        }.collect {
                            if (it != null) {
                                centerTv.visibility = View.GONE
                                unexpectedErrorTv.visibility = View.GONE
                                notFoundErrorTv.visibility = View.GONE

//                                Toast.makeText(
//                                    requireContext(),
//                                    "Found product ${it.name}",
//                                    Toast.LENGTH_SHORT
//                                ).show()

                                Constants.productEntity = it
                                findNavController().navigate(R.id.product_details_fragment)
                                requireContext().unregisterReceiver(this@BarcodeScanningListener)
                            } else {
                                centerTv.visibility = View.GONE
                                unexpectedErrorTv.visibility = View.GONE
                                notFoundErrorTv.visibility = View.VISIBLE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    if (_binding != null) {
                                        centerTv.visibility = View.VISIBLE
                                        unexpectedErrorTv.visibility = View.GONE
                                        notFoundErrorTv.visibility = View.GONE
                                    } else {
                                        Log.e("ABC", "Error")
                                    }
                                }, 2000)
                            }
                        }
                }
            }
//            else {
//                Log.d(TAG, "onReceive: Scanner bulmadi")
//                centerTv.visibility = View.GONE
//                unexpectedErrorTv.visibility = View.VISIBLE
//                notFoundErrorTv.visibility = View.GONE
//                Handler(Looper.getMainLooper()).postDelayed({
//                    if (_binding != null) {
//                        centerTv.visibility = View.VISIBLE
//                        unexpectedErrorTv.visibility = View.GONE
//                        notFoundErrorTv.visibility = View.GONE
//                    } else {
//                        Log.e("ABC", "Error")
//                    }
//                }, 2000)
//            }
        }
    }
}