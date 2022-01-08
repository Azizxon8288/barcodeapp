package com.example.barcodeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.barcodeapp.databinding.FragmentAboutBinding
import com.example.barcodeapp.models.Lists


class AboutFragment : Fragment() {
    private var lists: Lists? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lists = it.getSerializable("lists") as Lists?
        }
    }

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.apply {
            backBtn.setOnClickListener { findNavController().popBackStack() }
            tv.text = lists?.name
            image.setImageResource(lists!!.image)
            des.text = lists?.description
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}