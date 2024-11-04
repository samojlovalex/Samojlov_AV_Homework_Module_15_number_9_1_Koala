package com.example.samojlov_av_homework_module_15_number_9_1_koala.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_15_number_9_1_koala.R
import com.example.samojlov_av_homework_module_15_number_9_1_koala.databinding.FragmentChequeBinding
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.BasketAdapter
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChequeFragment : Fragment() {

    private lateinit var binding: FragmentChequeBinding
    private lateinit var recyclerChequeRV: RecyclerView
    private lateinit var deleteButtonStoreFBT: FloatingActionButton
    private lateinit var totalExpensesChequeTV: TextView

    private lateinit var viewModel: ProductViewModel
    private var adapter: BasketAdapter? = null
    private var listProducts: List<Product>? = null
    private var sum = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChequeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("DefaultLocale")
    private fun init() {
        recyclerChequeRV = binding.recyclerChequeRV
        deleteButtonStoreFBT = binding.deleteButtonStoreFBT
        totalExpensesChequeTV = binding.totalExpensesChequeTV

        val getSum = arguments?.getDouble("sumOut")
        if (getSum != null) {
            sum = getSum
        }
        val sumString = String.format("%.2f", sum).replace(',', '.')
        totalExpensesChequeTV.text = sumString

        recyclerChequeRV.layoutManager = LinearLayoutManager(context)

        deleteButtonStoreFBT.setOnClickListener {
            viewModel.deleteAllProduct()
            totalExpensesChequeTV.text = getString(R.string.totalExpensesCheque_null_text)
            Toast.makeText(context, getString(R.string.deleteButton_toast), Toast.LENGTH_LONG)
                .show()
        }

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProductViewModel::class.java]

        initAdapter()


    }


    private fun lifeDataProduct() {
        viewModel.products.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter?.updateList(it)
                listProducts = list
            }
        }
    }

    private fun initAdapter() {
        adapter = BasketAdapter(requireContext().applicationContext)
        recyclerChequeRV.adapter = adapter
        lifeDataProduct()
    }
}