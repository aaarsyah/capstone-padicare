package com.example.padicareapp.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null
)

data class ArticlesItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
