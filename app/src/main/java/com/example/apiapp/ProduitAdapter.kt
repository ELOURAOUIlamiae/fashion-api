// ProduitAdapter.kt
package com.example.apiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiapp.Produit

class ProduitAdapter(
    private var products: List<Produit>,
    // 👈 Zidna had l-listener
    private val onItemClicked: (Produit) -> Unit
) : RecyclerView.Adapter<ProduitAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.product_image)
        val title: TextView = view.findViewById(R.id.product_name)
        val price: TextView = view.findViewById(R.id.product_price)
        val description: TextView = view.findViewById(R.id.productDescription)
        val starIcon: ImageView = view.findViewById(R.id.star_icon) // 👈 Zidna hadi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.title.text = product.title
        holder.price.text = "${product.price} $"
        // Hna khass y-koun product.description
        holder.description.text = product.description

        Glide.with(holder.image.context)
            .load(product.image)
            .into(holder.image)

        // Gestion dyal l-Click
        holder.itemView.setOnClickListener {
            onItemClicked(product)
        }
    }

    fun updateList(newList: List<Produit>) {
        products = newList
        notifyDataSetChanged()
    }
}