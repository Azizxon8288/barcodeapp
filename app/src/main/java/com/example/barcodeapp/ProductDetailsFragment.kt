package com.example.barcodeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.barcodeapp.databinding.FragmentAboutBinding
import com.example.barcodeapp.functions.Constants.productEntity


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

    private lateinit var handler: Handler

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val productEntity = productEntity
        handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            findNavController().popBackStack()
        }, 15000)



        binding.apply {
            tv.isSelected = true
            tv.isSingleLine = true

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            tv.text = productEntity.name
            if (productEntity.measurement.isEmpty()) {
                piece.text = "1 null --> ${productEntity.salesPrice}"
            } else {
                piece.text = "1 ${productEntity.measurement} --> ${productEntity.salesPrice}"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}