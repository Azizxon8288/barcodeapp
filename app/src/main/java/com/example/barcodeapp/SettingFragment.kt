package com.example.barcodeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.barcodeapp.databinding.FragmentSettingBinding
import com.example.barcodeapp.dialog.DialogInput
import com.example.barcodeapp.functions.getFirst
import com.example.barcodeapp.functions.getIpAddress

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.tvIp.text = getIpAddress(requireActivity())
        binding.tvIp.setOnClickListener {
            val dialogInput = DialogInput(true)
            dialogInput.show(childFragmentManager, "INPUT_DIALOG")
        }
        binding.tvFirst.text = getFirst(requireActivity())
        binding.tvFirst.setOnClickListener {
            val dialogInput = DialogInput(false)
            dialogInput.show(childFragmentManager, "INPUT_DIALOG")
        }
        return binding.root
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SettingFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}