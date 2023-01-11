package com.example.barcodeapp.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.barcodeapp.databinding.DialogInputBinding
import com.example.barcodeapp.functions.getFirst
import com.example.barcodeapp.functions.getIpAddress
import com.example.barcodeapp.functions.setFirst
import com.example.barcodeapp.functions.setIpAddress


data class DialogInput(var isHave: Boolean) : DialogFragment() {

    private var _binding: DialogInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogInputBinding.inflate(inflater, container, false)
        if (isHave) {
            binding.ipTv.setText(getIpAddress(requireActivity()))
            binding.confirm.setOnClickListener {
                if (binding.ipTv.text != null && binding.ipTv.text.toString().isNotEmpty() && binding.ipTv.text.toString().trim().isNotEmpty()
                ) {
                    val ipAddress = binding.ipTv.text.toString()
                    setIpAddress(ipAddress, requireActivity())
                    binding.ipTv.setText(ipAddress)
                    Log.e("ip",ipAddress)
                    Toast.makeText(requireContext(), "Success Ip", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    binding.ipTv.requestFocus()
                }
            }
        } else {
            binding.address.text = "Screen"
            binding.ipTv.setText(getFirst(requireActivity()))
            binding.confirm.setOnClickListener {
                if (binding.ipTv.text != null && binding.ipTv.text.toString().isNotEmpty() && binding.ipTv.text.toString()
                        .trim().isNotEmpty()
                ) {
                    val screen = binding.ipTv.text.toString()
                    setFirst(screen, requireActivity())
                    binding.ipTv.setText(screen)
                    Toast.makeText(requireContext(), "Success Screen", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    binding.ipTv.requestFocus()
                }
            }
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }
        return binding.root


    }

}