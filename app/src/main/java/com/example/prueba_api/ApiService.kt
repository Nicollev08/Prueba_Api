package com.example.prueba_api

import retrofit2.Call

import retrofit2.http.GET

interface ApiService {
    @GET("tips")
    fun getTips(): Call<List<Tip>>
}