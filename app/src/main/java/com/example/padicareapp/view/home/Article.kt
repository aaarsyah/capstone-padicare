package com.example.padicareapp.view.home

data class Article(
    val id: Comparable<*>,
    val title: String,
    val summary: String,
    val content: String,
    val imageUrl: String,
    val publishedDate: String,
    val source: String,
)
