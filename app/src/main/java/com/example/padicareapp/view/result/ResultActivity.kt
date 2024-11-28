package com.example.padicareapp.view.result

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.padicareapp.R
import com.example.padicareapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data from the intent
        val diseaseName = intent.getStringExtra("disease_name") ?: "Unknown"
        val confidenceScore = intent.getFloatExtra("accuracy", 0.0f)
        val imageUriString = intent.getStringExtra("imageUri")

        // Display image, result, and additional details
        displayResults(diseaseName, confidenceScore, imageUriString)
    }

    private fun displayResults(diseaseName: String, confidenceScore: Float, imageUriString: String?) {
        // Display the selected image
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.resultImage.setImageBitmap(bitmap)
        }

        // Set disease detection result and confidence
        val resultText = "$diseaseName"
        binding.resultText.text = resultText

        val acuracyText = "Akurasi : ${confidenceScore * 100}%"
        binding.accuracyText.text = acuracyText

        // Provide disease-specific description and prevention tips
        when (diseaseName) {
            "Brown Spot" -> {
                binding.diseaseDescription.text = getString(R.string.disease_a_description)
                binding.preventionTips.text = getString(R.string.disease_a_prevention)
            }
            "Hispa" -> {
                binding.diseaseDescription.text = getString(R.string.disease_b_description)
                binding.preventionTips.text = getString(R.string.disease_b_prevention)
            }
            "Leaf Blast (Hawar Daun)" -> {
                binding.diseaseDescription.text = getString(R.string.disease_c_description)
                binding.preventionTips.text = getString(R.string.disease_c_prevention)
            }
            "Sehat" -> {
                binding.diseaseDescription.text = getString(R.string.disease_d_description)
                binding.preventionTips.text = getString(R.string.disease_d_prevention)
            }
            else -> {
                binding.diseaseDescription.text = getString(R.string.unknown_disease_description)
                binding.preventionTips.text = getString(R.string.unknown_disease_prevention)
            }
        }
    }
}
