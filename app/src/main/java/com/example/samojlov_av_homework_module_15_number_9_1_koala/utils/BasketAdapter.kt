package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_15_number_9_1_koala.R
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product

class BasketAdapter(private val context: Context) :
    RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {
    private val products = ArrayList<Product>()

    private var onBasketClickListener: OnBasketClickListener? = null

    interface OnBasketClickListener {
        fun onBasketClick(product: Product, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Product>) {
        products.clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }

    inner class BasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.nameBasketTV)
        private val imageIV: ImageView = itemView.findViewById(R.id.imageBasketItemIV)
        private val priceTV: TextView = itemView.findViewById(R.id.priceBasketTV)
        private val countTV: TextView = itemView.findViewById(R.id.countBasketTV)
        private val totalPriceTV: TextView = itemView.findViewById(R.id.totalPriceTV)

        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bind(product: Product) {
            nameTV.text = product.name
            imageIV.setImageResource(product.image)
            priceTV.text = "${product.price}\n${product.currency}"
            countTV.text = product.count.toString()
            val sum = product.price * product.count
            val sumString = String.format("%.2f", sum).replace(',', '.')
            totalPriceTV.text = "$sumString ${product.currency}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val viewHolder = BasketViewHolder(
            LayoutInflater.from(context).inflate(R.layout.basket_item, parent, false)
        )
        return viewHolder
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val currentProduct = products[position]
        holder.bind(currentProduct)

        holder.itemView.setOnClickListener {
            if (onBasketClickListener != null){
                onBasketClickListener!!.onBasketClick(currentProduct,position)
            }
        }
    }
    fun setOnBasketClickListener(onBasketClickListener: OnBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener
    }
}