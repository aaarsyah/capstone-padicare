package com.example.padicareapp.view.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padicareapp.R
import com.example.padicareapp.databinding.FragmentHomeBinding

class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val logoDrawable = resources.getDrawable(R.drawable.logo_tanpatulisan, null)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_tanpatulisan)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false)
        toolbar.logo = BitmapDrawable(resources, scaledBitmap)

        setupRecyclerViews()
        displayDummyData()
    }

    private fun setupRecyclerViews() {
        // Setup Disease RecyclerView
        diseaseAdapter = DiseaseAdapter { /* No-op for now */ }
        binding.recyclerPenyakit.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = diseaseAdapter
        }

        // Setup Article RecyclerView
        articleAdapter = ArticleAdapter { /* No-op for now */ }
        binding.recyclerArtikel.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
        }
    }

    private fun displayDummyData() {
        // Dummy data for diseases
        val dummyDiseases = listOf(
            Disease("1", "Blast Disease", "https://plantwiseplusknowledgebank.org/cms/asset/93dab454-de57-4946-bf39-6b9aeb8a015d/management-of-rice-blast-bangladesh-1.jpg"),
            Disease("2", "Brown Spot", "https://upload.wikimedia.org/wikipedia/commons/9/9c/Cochliobolus_miyabeanus.jpg"),
            Disease("3", "Tungro", "https://gapoktansekarsari.wordpress.com/wp-content/uploads/2016/07/4.png")
        )
        diseaseAdapter.submitList(dummyDiseases)

        // Dummy data for articles
        val dummyArticles = listOf(
            Article("1", "5 Tips for Healthy Crops", "Learn how to improve your harvest.", "https://example.com/article1.jpg"),
            Article("2", "Understanding Rice Diseases", "A comprehensive guide to rice diseases.", "https://example.com/article2.jpg"),
            Article("3", "Pest Control 101", "Effective pest management strategies.", "https://example.com/article3.jpg")
        )
        articleAdapter.submitList(dummyArticles)

        // Hide loading indicator
        binding.loadingIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
