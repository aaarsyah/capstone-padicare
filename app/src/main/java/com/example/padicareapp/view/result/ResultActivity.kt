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
        val confidenceScore = intent.getStringExtra("accuracy")
        val imageUriString = intent.getStringExtra("imageUri")

        // Display image, result, and additional details
        displayResults(diseaseName, confidenceScore, imageUriString)
    }

    private fun displayResults(
        diseaseName: String,
        confidenceScore: String?,
        imageUriString: String?
    ) {
        // Display the selected image
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.resultImage.setImageBitmap(bitmap)
        }

        // Set disease detection result and confidence
        binding.resultText.text = diseaseName

        val acuracyText = "Akurasi : $confidenceScore%"

        binding.accuracyText.text = acuracyText

        // Provide disease-specific description and prevention tips
        when (diseaseName) {
            "brownspot" -> {
                binding.diseaseDescription.text = getString(R.string.disease_a_description)
                binding.preventionTips.text = getString(R.string.disease_a_prevention)
            }

            "hispa" -> {
                binding.diseaseDescription.text = getString(R.string.disease_b_description)
                binding.preventionTips.text = getString(R.string.disease_b_prevention)
            }

            "leafblast" -> {
                binding.diseaseDescription.text = getString(R.string.disease_c_description)
                binding.preventionTips.text = getString(R.string.disease_c_prevention)
            }

            "healthy" -> {
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
