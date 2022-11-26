package com.example.aop_part3_chapter04.api

import com.example.aop_part3_chapter04.model.BestSellerDto
import com.example.aop_part3_chapter04.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/api/search.api")
    fun getBookByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>
    @GET("/api/bestSaller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>
}