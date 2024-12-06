package com.example.padicareapp.data.retrofit

import com.example.padicareapp.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.*
interface ApiService {
    @GET("artikel-padicare")
    fun getArticle(): Call<ArticleResponse>
}