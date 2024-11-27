package com.example.padicareapp.view.history

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padicareapp.R
import com.example.padicareapp.databinding.FragmentHistoryBinding

class FragmentHistory : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyAdapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
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

        setupRecyclerView()
        setupDeleteButton()

        // Example: Populating the RecyclerView with dummy data
        val dummyData = listOf(
            PredictionHistory("https://plantwiseplusknowledgebank.org/cms/asset/93dab454-de57-4946-bf39-6b9aeb8a015d/management-of-rice-blast-bangladesh-1.jpg", "Blast Disease", "90%"),
            PredictionHistory("https://upload.wikimedia.org/wikipedia/commons/9/9c/Cochliobolus_miyabeanus.jpg", "Brown Spot", "85%"),
            PredictionHistory("https://gapoktansekarsari.wordpress.com/wp-content/uploads/2016/07/4.png", "Tungro", "78%")
        )
        historyAdapter.submitList(dummyData)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun setupDeleteButton() {
        binding.btnDelete.setOnClickListener {
            Toast.makeText(context, getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
            // Example: Clear data from adapter
            historyAdapter.submitList(emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
