package com.example.barcodeapp

import android.annotation.SuppressLint
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

    lateinit var unexpectedErrorTv : TextView
    lateinit var notFoundErrorTv : TextView
    lateinit var centerTv: TextView

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

        unexpectedErrorTv = binding.tvUnexpectedError
        notFoundErrorTv = binding.tvNotFoundError
        centerTv = binding.tv

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
            if ("ok" == scanStatus) {
                Log.d(TAG, "onReceive barcode: $scannedBarcode")
                lifecycleScope.launch {
                    repository.searchByBarCode(scannedBarcode)
                        .catch {
                            centerTv.visibility = View.GONE
                            unexpectedErrorTv.visibility = View.VISIBLE
                            notFoundErrorTv.visibility = View.GONE
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (_binding != null) {
                                    centerTv.visibility = View.VISIBLE
                                    unexpectedErrorTv.visibility = View.GONE
                                    notFoundErrorTv.visibility = View.GONE
                                }else{
                                 Log.e("ABC","Error")
                                }
                            },2000)
                        }.collect {
                            if (it != null) {
                                centerTv.visibility = View.GONE
                                unexpectedErrorTv.visibility = View.GONE
                                notFoundErrorTv.visibility = View.GONE

                                Toast.makeText(
                                    requireContext(),
                                    "Found product ${it.name}",
                                    Toast.LENGTH_SHORT
                                ).show()

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
                                    }else{
                                        Log.e("ABC","Error")
                                    }
                                },2000)
                            }
                        }
                }
            } else {
                Log.d(TAG, "onReceive: Scanner bulmadi")
                centerTv.visibility = View.GONE
                unexpectedErrorTv.visibility = View.VISIBLE
                notFoundErrorTv.visibility = View.GONE
                Handler(Looper.getMainLooper()).postDelayed({
                    if (_binding != null) {
                        centerTv.visibility = View.VISIBLE
                        unexpectedErrorTv.visibility = View.GONE
                        notFoundErrorTv.visibility = View.GONE
                    }else{
                        Log.e("ABC","Error")
                    }
                },2000)
            }
        }

    }
}