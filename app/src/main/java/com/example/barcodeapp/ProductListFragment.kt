package com.example.barcodeapp

//import com.example.barcodeapp.adapters.ListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.barcodeapp.adapters.ListAdapter
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.databinding.FragmentListBinding
import com.example.barcodeapp.functions.Constants.categoryEntity
import com.example.barcodeapp.functions.Constants.productEntity
import com.example.barcodeapp.functions.navOptions
import com.example.barcodeapp.repository.CodeRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

//private const val ARG_PARAM1 = "category"

class CategoriesFragment : Fragment() {
//    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            category = it.getSerializable("category") as Category?
        }
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListAdapter
    private lateinit var repository: CodeRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var list: List<ProductEntity>
    private lateinit var category: CategoryEntity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())
        repository = CodeRepository(appDatabase, ApiClient.webservice)
//        val category = arguments?.getSerializable("category") as CategoryEntity
        category = categoryEntity
        binding.tv.text = category.name
        lifecycleScope.launch {
            repository.getDbProductByCategoryId(category.id).catch {
                binding.rv.visibility = View.GONE
                binding.errorTv.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Baza mavjud emas", Toast.LENGTH_SHORT).show()
            }.collect {
                binding.rv.visibility = View.VISIBLE
                binding.errorTv.visibility = View.GONE
                list = it

                listAdapter = ListAdapter(it, object : ListAdapter.OnItemClickListener {
                    override fun onItemClick(product: ProductEntity) {
                        val bundle = Bundle()
                        productEntity = product
                        bundle.putSerializable("product", product)
                        findNavController().navigate(R.id.product_details_fragment, bundle, navOptions())
                    }
                })
            }

        }


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchButtun.setOnClickListener {
            binding.tv.visibility = View.GONE
            binding.searchButtun.visibility = View.GONE
            binding.searchView.isIconified = false
            binding.searchView.visibility = View.VISIBLE

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    search(newText)
                }
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            binding.searchView.visibility = View.GONE
            binding.searchButtun.visibility = View.VISIBLE
            binding.tv.visibility = View.VISIBLE
            true
        }

        binding.rv.adapter = listAdapter
        return binding.root

    }

    fun search(str: String) {
        if (str.isNotEmpty()) {
            val query = "%$str%"
            list = ArrayList(repository.nameAndCodeSearchList(query, category.id))
            listAdapter.list = list
            listAdapter.notifyDataSetChanged()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            repository.getDbProductByCategoryId(categoryId = category.id).catch {
                binding.rv.visibility = View.GONE
                binding.errorTv.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Baza mavjud emas", Toast.LENGTH_SHORT).show()
            }.collect {
                binding.rv.visibility = View.VISIBLE
                binding.errorTv.visibility = View.GONE
                list = it
            }

        }
    }
}