package com.example.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.network.data.Product
import com.example.home.presentation.R
import com.example.core.R as coreR

class HorizontalSliderAdapter(
    private var itemList : List<Product>,
    private val context : Context,
    private val  onClickHandler: OnClickHandler
) : RecyclerView.Adapter<HorizontalSliderAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var card : CardView
        var title : TextView
        var price : TextView
        var image : ImageView
        init {
            itemView.apply {
                card = findViewById(R.id.horizontal_slider_maincardview)
                image = findViewById(R.id.horizontal_slider_imageview)
                title = findViewById(R.id.horizontal_slider_titletv)
                price = findViewById(R.id.horizontal_slider_priceTv)

                setOnClickListener {
                    onClickHandler.onItemClick(adapterPosition)
                }
            }
        }

        fun bind(product: Product) {
            title.text = product.title
            Glide.with(context).load(product.image).into(image)
            price.text = context.getString(coreR.string.price, product.price.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.horizontal_slider_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return if(itemList.isEmpty()) 0 else Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = itemList[position%itemList.size]
        holder.bind(item)
    }

    fun setData(data : List<Product>){
        itemList = data
        notifyDataSetChanged()
    }

    interface OnClickHandler {
        fun onItemClick(position: Int)
    }
}