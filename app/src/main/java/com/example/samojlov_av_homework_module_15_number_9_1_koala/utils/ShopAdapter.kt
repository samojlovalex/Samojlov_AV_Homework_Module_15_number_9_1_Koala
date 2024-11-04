package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_15_number_9_1_koala.R
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.ProductShop

class ShopAdapter(private val list: MutableList<ProductShop>) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    private var onShopClickListener: OnShopClickListener? = null

    interface OnShopClickListener {
        fun onShopClick(productShop: ProductShop, position: Int)
    }

    class ShopViewHolder(itemVieW: View) : RecyclerView.ViewHolder(itemVieW) {
        val image: ImageView = itemVieW.findViewById(R.id.imageShopItemIV)
        val name: TextView = itemVieW.findViewById(R.id.nameShopTV)
        val price: TextView = itemVieW.findViewById(R.id.priceShopTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val itemVieW = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_item, parent, false)
        return ShopViewHolder(itemVieW)
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val productsShop = list[position]
        holder.name.text = productsShop.name
        holder.price.text = "${productsShop.price} ${productsShop.currency}"
        holder.image.setImageResource(productsShop.image)

        holder.itemView.setOnClickListener {
            if (onShopClickListener != null) {
                onShopClickListener!!.onShopClick(productsShop, position)
            }
        }
    }

    fun setOnProductShopClickListener(onShopClickListener: OnShopClickListener) {
        this.onShopClickListener = onShopClickListener
    }

}