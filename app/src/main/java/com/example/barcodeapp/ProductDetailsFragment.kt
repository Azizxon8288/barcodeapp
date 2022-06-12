package com.example.barcodeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.barcodeapp.databinding.FragmentAboutBinding
import com.example.barcodeapp.functions.Constants.productEntity
import com.example.barcodeapp.functions.formatNumber


class ProductDetailsFragment : Fragment() {
//    private var product: ProductEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            product = it.getSerializable("product") as ProductEntity?
        }
    }

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ProductDetailsFragment"

//    private lateinit var appDatabase: AppDatabase
//    private lateinit var networkHelper: NetworkHelper
//    private lateinit var repository: CodeRepository
//    private lateinit var categoryViewModel: CategoryViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

//        appDatabase = AppDatabase.getInstance(requireContext())
//        networkHelper = NetworkHelper(requireContext())
//        repository = CodeRepository(appDatabase, ApiClient.webservice)
//        categoryViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(repository, networkHelper)
//        )[CategoryViewModel::class.java]
        val productEntity = productEntity

        Handler(Looper.getMainLooper()).postDelayed({
            if (_binding != null) {
                Log.w("ProductDetailsFragment","binding is not null")
                findNavController().popBackStack()
            } else {
                Log.w("ProductDetailsFragment","binding is null")
            }
        }, 5000)

//        val barcodeReceiver = BarcodeScanningListener()
//        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
//        requireContext().registerReceiver(barcodeReceiver, intentFilter)


        binding.apply {
//            tv.isSelected = true
//            tv.isSingleLine = true
//
//            backBtn.setOnClickListener {
//                findNavController().popBackStack()
//            }
            tv1.text = productEntity.name
            if (productEntity.measurement.isEmpty()) {
                piece.text = "1 null --> ${productEntity.salesPrice}"
            } else {
                piece.text = "${formatNumber(productEntity.salesPrice)}/${productEntity.measurement}"
            }
            //            image.setImageResource(R.drawable.ic_launcher_background)
            if (productEntity.description.isEmpty()) {
//                des.text = "null"
                des.visibility = View.GONE
            } else {
                des.visibility = View.VISIBLE
                des.text = productEntity.description
            }


        }
        return binding.root
    }


//    inner class BarcodeScanningListener : BroadcastReceiver() {
//        @SuppressLint("UnsafeProtectedBroadcastReceiver")
//        override fun onReceive(p0: Context?, p1: Intent?) {
//            val scannedBarcode = p1?.getStringExtra("SCAN_BARCODE1")
//            val scanStatus = p1?.getStringExtra("SCAN_STATE")
//            if ("ok" == scanStatus) {
//                Log.d(TAG, "onReceive barcode: $scannedBarcode")
//                lifecycleScope.launch {
//                    repository.searchByBarCode(scannedBarcode)
//                        .catch {
////                            centerTv.visibility = View.GONE
////                            unexpectedErrorTv.visibility = View.VISIBLE
////                            notFoundErrorTv.visibility = View.GONE
////                            Handler(Looper.getMainLooper()).postDelayed({
////                                if (_binding != null) {
////                                    centerTv.visibility = View.VISIBLE
////                                    unexpectedErrorTv.visibility = View.GONE
////                                    notFoundErrorTv.visibility = View.GONE
////                                }else{
////                                    Log.e("ABC","Error")
////                                }
////                            },2000)
//                        }.collect {
//                            if (it != null) {
////                                centerTv.visibility = View.GONE
////                                unexpectedErrorTv.visibility = View.GONE
////                                notFoundErrorTv.visibility = View.GONE
//
////                                Toast.makeText(
////                                    requireContext(),
////                                    "Found product ${it.name}",
////                                    Toast.LENGTH_SHORT
////                                ).show()
//
//                                productEntity = it
////                                findNavController().navigate(R.id.product_details_fragment)
////                                requireContext().unregisterReceiver(this@BarcodeScanningListener)
//                            } else {
////                                centerTv.visibility = View.GONE
////                                unexpectedErrorTv.visibility = View.GONE
////                                notFoundErrorTv.visibility = View.VISIBLE
////                                Handler(Looper.getMainLooper()).postDelayed({
////                                    if (_binding != null) {
////                                        centerTv.visibility = View.VISIBLE
////                                        unexpectedErrorTv.visibility = View.GONE
////                                        notFoundErrorTv.visibility = View.GONE
////                                    }else{
////                                        Log.e("ABC","Error")
////                                    }
////                                },2000)
//                                Log.e("12","12")
//                            }
//                        }
//                }
//            } else {
//                Log.d(TAG, "onReceive: Scanner bulmadi")
////                centerTv.visibility = View.GONE
////                unexpectedErrorTv.visibility = View.VISIBLE
////                notFoundErrorTv.visibility = View.GONE
////                Handler(Looper.getMainLooper()).postDelayed({
////                    if (_binding != null) {
////                        centerTv.visibility = View.VISIBLE
////                        unexpectedErrorTv.visibility = View.GONE
////                        notFoundErrorTv.visibility = View.GONE
////                    }else{
////                        Log.e("ABC","Error")
////                    }
////                },2000)
//            }
//        }
//
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}