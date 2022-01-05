package com.example.barcodeapp

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barcodeapp.adapters.CategoryAdapter
import com.example.barcodeapp.databinding.FragmentHomeBinding
import com.example.barcodeapp.models.Category
import android.content.Intent
import android.content.IntentFilter
import com.example.barcodeapp.broadCast.MyReceiver


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentHomeBinding? = null
    private lateinit var categoryAdapter: CategoryAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        categoryAdapter = CategoryAdapter(loadData())
        val intent = Intent("nlscan.action.SCANNER_TRIG")
        intent.putExtra("SCAN_TIMEOUT", 4)
        requireContext().sendBroadcast(intent)

        val mFilter = IntentFilter("nlscan.action.SCANNER_RESULT");
        val myReceiver = MyReceiver()
        requireContext().registerReceiver(myReceiver, mFilter)

        binding.apply {
            rv.adapter = categoryAdapter
        }


        return binding.root
    }


    private fun loadData(): List<Category> {
        val list = ArrayList<Category>()
        list.add(Category("Oziq-Ovqatlar", R.drawable.ic_launcher_background))
        list.add(Category("Mevalar", R.drawable.ic_launcher_foreground))
        list.add(Category("Sabzavotlar", R.drawable.ic_launcher_background))
        list.add(Category("Ichimliklar", R.drawable.ic_launcher_foreground))
        list.add(Category("Ichimliklar", R.drawable.ic_launcher_background))
        list.add(Category("Oziq-Ovqatlar", R.drawable.ic_launcher_background))
        list.add(Category("Mevalar", R.drawable.ic_launcher_foreground))
        list.add(Category("Sabzavotlar", R.drawable.ic_launcher_background))
        list.add(Category("Ichimliklar", R.drawable.ic_launcher_foreground))
        list.add(Category("Ichimliklar", R.drawable.ic_launcher_background))
        return list
    }

}