package com.example.apiapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Produit>>
    @GET("products/{id}")
    fun getProduitDetail(@Path("id") produitId: Int): Call<Produit>
}