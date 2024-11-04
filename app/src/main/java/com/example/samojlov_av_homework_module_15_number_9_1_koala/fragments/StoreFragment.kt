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
import com.example.samojlov_av_homework_module_15_number_9_1_koala.databinding.FragmentStoreBinding
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.ProductViewModel
import com.example.samojlov_av_homework_module_15_number_9_1_koala.utils.ShopAdapter
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.ProductShop
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class StoreFragment : Fragment() {

    private lateinit var binding: FragmentStoreBinding
    private lateinit var recyclerStoreRV: RecyclerView
    private lateinit var floatButtonStoreFBT: FloatingActionButton
    private var adapter: ShopAdapter? = null

    private lateinit var viewModel: ProductViewModel
    private var listProducts: List<Product>? = null

    private var checkProduct = false
    private var currentCount = 1
    private var productShopClass: ProductShop? = null
    private var sum = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStoreBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    private fun init() {
        recyclerStoreRV = binding.recyclerStoreRV
        floatButtonStoreFBT = binding.floatButtonStoreFBT

        recyclerStoreRV.layoutManager = LinearLayoutManager(context)

        floatButtonStoreFBT.setOnClickListener {
            if (listProducts != null) {
                if (listProducts!!.isEmpty()) return@setOnClickListener
            }
            totalExpenses()
            val bundle = Bundle()
            bundle.putDouble("sum", sum)
            val fragmentBasket = BasketFragment()
            fragmentBasket.arguments = bundle
            fragmentManager?.beginTransaction()
                ?.replace(R.id.containerFragmentSecondActivityFCV, fragmentBasket)?.commit()
        }

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProductViewModel::class.java]

        lifeDataProduct()

        initAdapter()
    }


    private fun lifeDataProduct() {
        viewModel.products.observe(viewLifecycleOwner) { list ->
            list?.let {
                listProducts = list
            }
        }
    }

    private fun initAdapter() {
        adapter = ShopAdapter(ProductShop.productsList)
        recyclerStoreRV.adapter = adapter
        recyclerStoreRV.setHasFixedSize(true)
        adapter!!.setOnProductShopClickListener(object : ShopAdapter.OnShopClickListener {
            override fun onShopClick(productShop: ProductShop, position: Int) {
                setProductShop(productShop)
            }
        })
    }

    private fun setProductShop(productShop: ProductShop) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogValues = inflater.inflate(R.layout.alter_dialog_item, null)
        dialogBuilder.setView(dialogValues)

        productShopClass = productShop


        setCount(dialogValues, productShopClass!!)

        dialogBuilder.setPositiveButton(getString(R.string.store_fragment_positive_button_alert_dialog)) { _, _ ->
            if (productShopClass != null) {
                if (checkProduct) {
                    var id = 0
                    for (i in listProducts!!) {
                        if (i.name == productShop.name && i.price == productShop.price) {
                            id = i.id
                        }
                    }
                    viewModel.updateProduct(
                        Product(
                            productShop.name,
                            productShop.price,
                            productShop.image,
                            productShop.currency,
                            currentCount,
                            id
                        )
                    )
                } else {
                    viewModel.insertProduct(
                        Product(
                            productShop.name,
                            productShop.price,
                            productShop.image,
                            productShop.currency,
                            currentCount
                        )
                    )
                }
            }
            checkProduct = false

        }

        dialogBuilder.setNeutralButton(getString(R.string.alter_dialog_neutral_text)) { _, _ ->
            if (checkProduct) {
                var id = 0
                for (i in listProducts!!) {
                    if (i.name == productShop.name && i.price == productShop.price) {
                        id = i.id
                    }
                }
                viewModel.deleteProduct(
                    Product(
                        productShop.name,
                        productShop.price,
                        productShop.image,
                        productShop.currency,
                        productShop.count,
                        id
                    )
                )
                Toast.makeText(
                    context,
                    getString(R.string.alter_dialog_neutral_button_toast, productShop.name),
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        dialogBuilder.setNegativeButton(getString(R.string.alert_dialog_negative_button), null)
        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        dialogBuilder.create().show()
    }

    @SuppressLint("SetTextI18n")
    private fun setCount(dialogValues: View?, productShop: ProductShop) {
        checkProduct = false

        currentCount = 1

        val minusButton = dialogValues?.findViewById<Button>(R.id.minusButtonAlertDialogBT)
        val count = dialogValues?.findViewById<TextView>(R.id.countAlertDialogTV)
        val plusButton = dialogValues?.findViewById<Button>(R.id.plusButtonAlertDialogBT)
        val image = dialogValues?.findViewById<ImageView>(R.id.imageAlterDialogIV)
        val price = dialogValues?.findViewById<TextView>(R.id.priceAlterDialogTV)

        image!!.setImageResource(productShop.image)
        price!!.text = "${productShop.price}\n${productShop.currency}"

        if (listProducts != null) {
            for (i in listProducts!!) {
                if (i.name == productShop.name && i.price == productShop.price) {
                    checkProduct = true
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

    @SuppressLint("SetTextI18n")
    private fun totalExpenses() {
        sum = 0.0
        if (listProducts != null) {
            if (listProducts!!.isNotEmpty()) {
                var purchase: Double
                for (product in listProducts!!) {
                    purchase = product.price * product.count
                    sum += purchase
                }
            }
        }
    }
}