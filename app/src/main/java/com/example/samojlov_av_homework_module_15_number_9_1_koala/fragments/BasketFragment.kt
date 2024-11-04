package com.example.samojlov_av_homework_module_15_number_9_1_koala.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_15_number_9_1_koala.R
import com.example.samojlov_av_homework_module_15_number_9_1_koala.databinding.FragmentBasketBinding
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.BasketAdapter
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.ProductViewModel
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.TotalViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private lateinit var recyclerBasketRV: RecyclerView
    private lateinit var totalExpensesTV: TextView
    private lateinit var backButtonStoreFBT: FloatingActionButton
    private lateinit var paymentButtonStoreFBT: FloatingActionButton
    private lateinit var sumViewModel: TotalViewModel

    private lateinit var viewModel: ProductViewModel
    private var adapter: BasketAdapter? = null
    private var listProducts: List<Product>? = null
    private var currentCount = 1
    private var sum = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        sumViewModel = ViewModelProvider(this)[TotalViewModel::class.java]

        sumViewModel.currentTotalSum.observe(viewLifecycleOwner) {
            totalExpensesTV.text = it
        }

        recyclerBasketRV = binding.recyclerBasketRV
        totalExpensesTV = binding.totalExpensesTV
        backButtonStoreFBT = binding.backButtonStoreFBT
        paymentButtonStoreFBT = binding.paymentButtonStoreFBT

        val getSum = arguments?.getDouble("sum")
        if (getSum != null) {
            sum = getSum
        }
        sumPrint()

        recyclerBasketRV.layoutManager = LinearLayoutManager(context)

        backButtonStoreFBT.setOnClickListener {
            val storeFragment = StoreFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.containerFragmentSecondActivityFCV, storeFragment)?.commit()
        }

        paymentButtonStoreFBT.setOnClickListener {
            if (listProducts != null) {
                if (listProducts!!.isEmpty()) return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putDouble("sumOut", sum)
            val chequeFragment = ChequeFragment()
            chequeFragment.arguments = bundle
            fragmentManager?.beginTransaction()
                ?.replace(R.id.containerFragmentSecondActivityFCV, chequeFragment)?.commit()
            Toast.makeText(context, getString(R.string.payment_toast), Toast.LENGTH_LONG).show()
        }

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProductViewModel::class.java]

        lifeDataProduct()

        initAdapter()
    }

    @SuppressLint("SetTextI18n")
    private fun totalExpenses(list: List<Product>) {
        sum = 0.0

        if (list.isNotEmpty()) {
            var purchase: Double
            for (product in list) {
                purchase = product.price * product.count
                sum += purchase
            }
        }

        sumPrint()
    }

    @SuppressLint("DefaultLocale")
    private fun sumPrint() {
        val sumString = String.format("%.2f", sum).replace(',', '.')
        sumViewModel.currentTotalSum.value = (sumString.also { sumViewModel.totalSum = it })
    }

    private fun initAdapter() {
        adapter = BasketAdapter(requireContext().applicationContext)
        recyclerBasketRV.adapter = adapter
        adapter!!.setOnBasketClickListener(object : BasketAdapter.OnBasketClickListener {
            override fun onBasketClick(product: Product, position: Int) {
                setProductBasket(product)
            }

        })
    }

    private fun setProductBasket(product: Product) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogValues = inflater.inflate(R.layout.alter_dialog_item, null)
        dialogBuilder.setView(dialogValues)

        setCount(dialogValues, product)

        dialogBuilder.setPositiveButton(getString(R.string.basket_alter_dialog_positive_burron_text)) { _, _ ->

            var id = 0
            for (i in listProducts!!) {
                if (i.name == product.name && i.price == product.price) {
                    id = i.id
                }
            }
            viewModel.updateProduct(
                Product(
                    product.name,
                    product.price,
                    product.image,
                    product.currency,
                    currentCount,
                    id
                )
            )


        }.also {
            if (listProducts != null) {
                totalExpenses(listProducts!!)
            }
        }

        dialogBuilder.setNeutralButton(getString(R.string.basket_alter_dialog_neutral_burron_text)) { _, _ ->

            var id = 0
            for (i in listProducts!!) {
                if (i.name == product.name && i.price == product.price) {
                    id = i.id
                }
            }
            viewModel.deleteProduct(
                Product(
                    product.name,
                    product.price,
                    product.image,
                    product.currency,
                    product.count,
                    id
                )
            )
            Toast.makeText(
                context,
                getString(R.string.alter_dialog_neutral_button_toast, product.name),
                Toast.LENGTH_LONG
            ).show()

        }.also {
            if (listProducts != null) {
                totalExpenses(listProducts!!)
            }
        }

        dialogBuilder.setNegativeButton(getString(R.string.alert_dialog_negative_button), null)
        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        dialogBuilder.create().show()
    }

    @SuppressLint("SetTextI18n")
    private fun setCount(dialogValues: View?, product: Product) {
        currentCount = 1

        val minusButton = dialogValues?.findViewById<Button>(R.id.minusButtonAlertDialogBT)
        val count = dialogValues?.findViewById<TextView>(R.id.countAlertDialogTV)
        val plusButton = dialogValues?.findViewById<Button>(R.id.plusButtonAlertDialogBT)
        val image = dialogValues?.findViewById<ImageView>(R.id.imageAlterDialogIV)
        val price = dialogValues?.findViewById<TextView>(R.id.priceAlterDialogTV)

        image!!.setImageResource(product.image)
        price!!.text = "${product.price}\n${product.currency}"

        if (listProducts != null) {
            for (i in listProducts!!) {
                if (i.name == product.name && i.price == product.price) {
                    if (i.count > 1) {
                        currentCount = i.count
                    }
                }
            }
        }

        count?.text = currentCount.toString()

        minusAlertDialogButton(minusButton, count)
        plusAlertDialogButton(plusButton, count)
    }

    private fun plusAlertDialogButton(plusButton: Button?, count: TextView?) {
        plusButton?.setOnClickListener {
            var countPlus = count?.text.toString().toInt()
            countPlus++
            count?.text = countPlus.toString()
            currentCount = count?.text.toString().toInt()
        }
    }

    private fun minusAlertDialogButton(minusButton: Button?, count: TextView?) {
        minusButton?.setOnClickListener {
            if (count?.text.toString().toInt() < 2) return@setOnClickListener
            var countMinus = count?.text.toString().toInt()
            countMinus--
            count?.text = countMinus.toString()
            currentCount = count?.text.toString().toInt()
        }
    }

    private fun lifeDataProduct() {
        viewModel.products.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter?.updateList(it)
                listProducts = list
            }
        }
    }

}