package com.example.apiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.apiapp.Produit
import com.example.apiapp.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. N-chddou l-ID li daz mn MainActivity
        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId != -1) {
            // 2. N-jibdou les détails dyal l-produit
            fetchProductDetails(productId)
        } else {
            // ID manquante
            Toast.makeText(this, "Erreur: ID de produit non trouvé.", Toast.LENGTH_LONG).show()
            finish() // N-r-j-jou l-l-back
        }

        // 3. Listener pour le bouton "Ajouter au panier"
        val btnAddToCart: Button = findViewById(R.id.btn_add_to_cart)
        btnAddToCart.setOnClickListener {
            Toast.makeText(this, "Produit ajouté au panier!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchProductDetails(id: Int) {
        // *******************************************************************
        // ATTENTION: On utilise getProducts() au lieu de getProduitDetail(id)
        // car cette dernière n'est pas définie dans votre ApiService.kt
        // On va chercher le produit dans la liste complète.
        // *******************************************************************
        RetrofitClient.instance.getProducts().enqueue(object : Callback<List<Produit>> {
            override fun onResponse(call: Call<List<Produit>>, response: Response<List<Produit>>) {
                if (response.isSuccessful && response.body() != null) {
                    // N-jibdou ghir l-produit li 3andou had l-ID
                    val product = response.body()!!.find { it.id == id }
                    if (product != null) {
                        displayProductDetails(product)
                    } else {
                        Toast.makeText(this@DetailActivity, "Produit introuvable.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "Réponse API non réussie.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Produit>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Erreur API: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayProductDetails(product: Produit) {
        // L-Attributs dyal l-Layout
        val imageView: ImageView = findViewById(R.id.detail_product_image)
        val nameView: TextView = findViewById(R.id.detail_product_name)
        val priceView: TextView = findViewById(R.id.detail_product_price)
        val descriptionView: TextView = findViewById(R.id.detail_product_description)

        // T-3emmar les Textes
        nameView.text = product.title
        priceView.text = "${product.price} $"
        descriptionView.text = product.description

        // T-chareji l-Image b-Glide
        Glide.with(this)
            .load(product.image)
            .placeholder(R.drawable.ic_launcher_foreground) // T-zedna placeholder 7ssn
            .into(imageView)

        // N-dirou l-Title dyal l-Activity howa l-Isem dyal l-Produit
        supportActionBar?.title = product.title
    }
}