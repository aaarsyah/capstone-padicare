package com.example.padicareapp.view.result

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.padicareapp.R
import com.example.padicareapp.databinding.ActivityResultBinding
import java.io.InputStream

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data passed from the previous activity
        val diseaseName = intent.getStringExtra("disease_name") ?: "Unknown Disease"
        val imageUriString = intent.getStringExtra("image_url") ?: ""
        val accuracy = intent.getStringExtra("accuracy") ?: "N/A"
        val description = intent.getStringExtra("description") ?: "No description available."
        val prevention = intent.getStringExtra("prevention") ?: "No prevention tips available."

        // Set the data to the views
        binding.resultText.text = diseaseName
        binding.accuracyText.text = "Akurasi: $accuracy"
        binding.diseaseDescription.text = description
        binding.preventionTips.text = prevention

        // Load the image depending on whether it's a URL or URI
        if (imageUriString.isNotEmpty()) {
            // If the image URL is a valid URI
            val imageUri = Uri.parse(imageUriString)
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.resultImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // In case of an error, set a default image or handle the exception
                binding.resultImage.setImageResource(R.drawable.baseline_photo_size_select_actual_24) // Example placeholder
            }
        } else {
            // Fallback if imageUriString is empty, use a default image
            binding.resultImage.setImageResource(R.drawable.baseline_photo_size_select_actual_24) // Example placeholder
        }
    }
}
