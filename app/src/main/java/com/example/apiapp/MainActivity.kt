package com.example.apiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
// ⚠️ Khassk t-zid l-import s-s7i7 dyal Produit w RetrofitClient,
// 7it f-l-code l-awwal kanou naqsin:
// import com.example.apiapp.data.Produit
// import com.example.apiapp.api.RetrofitClient (ila kano f-dossierat w-khriyin)
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProduitAdapter

    private var allProducts = listOf<Produit>()
    private var categories = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.myspinner)
        recyclerView = findViewById(R.id.myrecyclerview)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // T-Initialiser l-Adapter b-Click Listener
        adapter = ProduitAdapter(emptyList()) { produit ->
            onProductClicked(produit)
        }
        recyclerView.adapter = adapter

        fetchProducts()
    }

    private fun fetchProducts() {
        // ⚠️ N-f-tardou anna RetrofitClient w ApiService m-qaddin
        RetrofitClient.instance.getProducts().enqueue(object : Callback<List<Produit>> {
            override fun onResponse(call: Call<List<Produit>>, response: Response<List<Produit>>) {
                if (response.isSuccessful && response.body() != null) {
                    allProducts = response.body()!!

                    // T-jib l-categories m3a "All Products" f-l-awal
                    val distinctCategories = allProducts.map { it.category }.distinct()
                    categories = listOf("All Products") + distinctCategories
                    setupSpinner()

                    // N-affichiw l-products kamlin f-l-awal
                    adapter.updateList(allProducts)
                }
            }

            override fun onFailure(call: Call<List<Produit>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "API Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupSpinner() {
        val spinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = categories[position]

                if (selected == "All Products") {
                    adapter.updateList(allProducts)
                } else {
                    val filtered = allProducts.filter { it.category == selected }
                    adapter.updateList(filtered)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // L-Fonction li kat-khdem mli t-cliki 3la produit
    private fun onProductClicked(produit: Produit) {
        // Toast.makeText(this, "Vous avez cliqué sur : ${produit.title}", Toast.LENGTH_SHORT).show()

        // ✅ Hada howa l-code l-m-corrige dyal l-Intent:
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("PRODUCT_ID", produit.id)
        startActivity(intent)
    }
}