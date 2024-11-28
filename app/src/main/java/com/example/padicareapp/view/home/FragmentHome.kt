package com.example.padicareapp.view.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padicareapp.R
import com.example.padicareapp.databinding.FragmentHomeBinding
import com.example.padicareapp.view.detaildisease.ActivityDetailDisease

class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModel: HomeViewModel by viewModels()

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
        observeViewModel()
        viewModel.fetchHomeData()
    }

    private fun setupRecyclerViews() {
        diseaseAdapter = DiseaseAdapter { disease ->
            val intent = Intent(requireContext(), ActivityDetailDisease::class.java).apply {
                putExtra("diseaseId", disease.id)
                putExtra("diseaseName", disease.name)
                putExtra("diseaseImage", disease.imageUrl)
                putExtra("diseaseDesc", disease.desc)
                putExtra("diseasePencegahan", disease.pencegahan)
                putExtra("diseasePengobatan", disease.pengobatan)
            }
            startActivity(intent)
        }

        binding.recyclerPenyakit.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = diseaseAdapter
        }

        articleAdapter = ArticleAdapter { /* Handle item click */ }
        binding.recyclerArtikel.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.diseases.observe(viewLifecycleOwner, Observer { diseases ->
            diseaseAdapter.submitList(diseases)
        })

        viewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            articleAdapter.submitList(articles)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
