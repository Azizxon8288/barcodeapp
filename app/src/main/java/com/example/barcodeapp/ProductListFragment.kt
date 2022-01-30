package com.example.barcodeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import com.example.barcodeapp.adapters.ListAdapter
import com.example.barcodeapp.databinding.FragmentListBinding

private const val ARG_PARAM1 = "category"

class ListFragment : Fragment() {
//    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            category = it.getSerializable("category") as Category?
        }
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
//    private lateinit var listAdapter: ListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
//        listAdapter = ListAdapter(loadData(), object : ListAdapter.OnItemClickListener {
//            override fun onItemClick(lists: Lists) {
//                val bundle = Bundle()
//                bundle.putSerializable("lists", lists)
//                findNavController().navigate(R.id.aboutFragment, bundle, navOptions())
//            }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}