package com.example.padicareapp.view.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.padicareapp.R

class ArticleAdapter(
    private val onItemClicked: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private val articleList = mutableListOf<Article>()

    // Function to submit a new list
    fun submitList(articles: List<Article>) {
        articleList.clear()
        articleList.addAll(articles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articleList.size

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.card_article)
        private val imageArticle: ImageView = itemView.findViewById(R.id.image_artikel)
        private val textArticleTitle: TextView = itemView.findViewById(R.id.text_artikel)
        private val textArticleDescription: TextView = itemView.findViewById(R.id.text_deskripsi_artikel)

        fun bind(article: Article) {
            textArticleTitle.text = article.title
            textArticleDescription.text = article.summary
            Glide.with(itemView.context).load(article.imageUrl).into(imageArticle)

            cardView.setOnClickListener {
                val intent = Intent(itemView.context, ArticleActivity::class.java).apply {
                putExtra(ArticleActivity.EXTRA_URL, article.source)
            }
                itemView.context.startActivity(intent)
            }
//
        }
    }
}
