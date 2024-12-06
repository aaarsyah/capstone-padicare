package com.example.padicareapp.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null
)

data class ArticlesItem(

	@field:SerializedName("summary")
	val summary: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("published_date")
	val publishedDate: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
