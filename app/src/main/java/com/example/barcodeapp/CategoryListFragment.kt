package com.example.barcodeapp

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
//import com.example.barcodeapp.adapters.CategoryAdapter
import com.example.barcodeapp.broadcast.BarcodeReceiver
import com.example.barcodeapp.databinding.FragmentHomeBinding


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

    //    private lateinit var categoryAdapter: CategoryAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        categoryAdapter = CategoryAdapter(loadData(), object : CategoryAdapter.OnItemClick {
//            override fun onItemClick(category: Category) {
//                val bundle = Bundle()
//                bundle.putSerializable("category", category)
//                findNavController().navigate(R.id.listFragment, bundle, navOptions())
//            }
//
//        })


        val barcodeReceiver = BarcodeReceiver()
        val intentFilter = IntentFilter("nlscan.action.SCANNER_RESULT");
        requireContext().registerReceiver(barcodeReceiver, intentFilter)

        binding.apply {
            moreBtn.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), moreBtn)
                popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { it1 ->
                    when (it1.itemId) {
                        R.id.settings -> Toast.makeText(
                            requireContext(),
                            "Settings",
                            Toast.LENGTH_SHORT
                        ).show()
                        R.id.icon -> Toast.makeText(requireContext(), "Icon", Toast.LENGTH_SHORT)
                            .show()
                    }
                    true
                }
                popupMenu.show()
            }
//            rv.adapter = categoryAdapter
        }


        return binding.root
    }


//    private fun loadData(): List<Category> {
//        val list = ArrayList<Category>()
//        list.add(Category("Oziq-Ovqatlar", R.drawable.food_svg))
//        list.add(Category("Mevalar", R.drawable.fruits_svg))
//        list.add(Category("Sabzavotlar", R.drawable.vegatable_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Oziq-Ovqatlar", R.drawable.food_svg))
//        list.add(Category("Mevalar", R.drawable.fruits_svg))
//        list.add(Category("Sabzavotlar", R.drawable.vegatable_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Ichimliklar", R.drawable.drinks_svg))
//        list.add(Category("Mevalar", R.drawable.fruits_svg))
//        list.add(Category("Oziq-Ovqatlar", R.drawable.food_svg))
//        list.add(Category("Sabzavotlar", R.drawable.vegatable_svg))
//        return list
//    }

}