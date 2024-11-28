package com.example.padicareapp.view.history

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padicareapp.R
import com.example.padicareapp.databinding.FragmentHistoryBinding
import com.example.padicareapp.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentHistory : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter

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

        adapter = HistoryAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Load history data
        loadHistory()

        // Set up delete all button
        binding.btnDelete.setOnClickListener {
            Toast.makeText(requireContext(), "Hapus Berhasil", Toast.LENGTH_SHORT).show()
            deleteAllHistory()
        }
    }

    private fun loadHistory() {
        val database = AppDatabase.getDatabase(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val historyList = database.predictionHistoryDao().getAllHistory()
            Log.d("HistoryFragment", "Loaded history: $historyList")
            withContext(Dispatchers.Main) {
                adapter.submitList(historyList)
            }
        }
    }

    private fun deleteAllHistory() {
        val database = AppDatabase.getDatabase(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            database.predictionHistoryDao().deleteAllHistory()
            withContext(Dispatchers.Main) {
                adapter.submitList(emptyList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}