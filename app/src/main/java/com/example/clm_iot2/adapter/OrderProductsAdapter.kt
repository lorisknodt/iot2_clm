package com.example.clm_iot2.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.clm_iot2.R

import com.example.clm_iot2.model.OrderProduct
import com.example.clm_iot2.security.TokenManager

class OrderProductsAdapter(private val products: List<OrderProduct>) :
    RecyclerView.Adapter<OrderProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_single_order, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productVariant: TextView = itemView.findViewById(R.id.productVariant)
        private val productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)

        fun bind(product: OrderProduct) {
            productName.text = product.name
            productVariant.text = product.description
            productQuantity.text = itemView.context.getString(R.string.singleorder_quantiy, product.quantity.toString())
            productPrice.text = String.format("%.2f €", product.price * product.quantity)

            val baseUrl = "https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net"
            val fullImageUrl = baseUrl + product.imageUrl

            val glideUrl = GlideUrl(
                fullImageUrl,
                LazyHeaders.Builder()
                    .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                    .build()
            )
            // Utiliser Glide pour charger l'image
            Glide.with(itemView.context)
                .load(glideUrl)
                .into(productImage)
        }
    }
}