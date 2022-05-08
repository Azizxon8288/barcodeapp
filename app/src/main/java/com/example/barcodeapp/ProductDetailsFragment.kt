package com.example.barcodeapp

import android.annotation.SuppressLint
import android.os.Bundle
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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val productEntity = productEntity


        binding.apply {
            tv.isSelected = true
            tv.isSingleLine = true

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            tv.text = productEntity.name
            image.setImageResource(R.drawable.ic_launcher_background)
            if (productEntity.description.isEmpty()) {
                des.text = "null"
            } else {
                des.text = productEntity.description
            }


            piece.text = "1 ${productEntity.measurement} --> ${productEntity.salesPrice}"

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}