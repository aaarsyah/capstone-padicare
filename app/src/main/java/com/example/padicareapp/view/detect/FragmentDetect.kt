package com.example.padicareapp.view.detect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.padicareapp.R
import com.example.padicareapp.databinding.FragmentDetectBinding
import com.example.padicareapp.entity.PredictionHistory
import com.example.padicareapp.helper.ImageClassifierHelper
import com.example.padicareapp.room.AppDatabase
import com.example.padicareapp.view.camera.CameraActivity
import com.example.padicareapp.view.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.padicareapp.view.result.ResultActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications

class FragmentDetect : Fragment(R.layout.fragment_detect), ImageClassifierHelper.ClassifierListener {

    private lateinit var binding: FragmentDetectBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null
    private val detectViewModel: DetectViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                currentImageUri = it
                detectViewModel.setCurrentImageUri(it)
            } ?: run {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CAMERAX_RESULT) {
                val uri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
                if (uri != null) {
                    currentImageUri = uri
                    detectViewModel.setCurrentImageUri(uri)
                }
            }
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetectBinding.bind(view)

        val toolbar = binding.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val logoDrawable = resources.getDrawable(R.drawable.logo_tanpatulisan, null)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_tanpatulisan)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false)
        toolbar.logo = BitmapDrawable(resources, scaledBitmap)

        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = this
        )

        detectViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            showImage(uri)
        }

        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCheck.setOnClickListener { detectDisease() }

        // Permissions check
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun showImage(imageUri: Uri?) {
        binding.ivImage.setImageURI(null)
        if (imageUri != null) {
            binding.ivImage.setImageURI(imageUri)
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun detectDisease() {
        val currentUri = detectViewModel.currentImageUri.value
        if (currentUri == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(requireContext(), "Detecting...", Toast.LENGTH_SHORT).show()

        imageClassifierHelper.classifyStaticImage(currentUri)

//        val fakeResultLabel = "Brown Spot" // Example label
//        val fakeConfidence = 0.95f // Example confidence score

//        savePredictionHistory(currentUri.toString(), fakeResultLabel, fakeConfidence)
//
//        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
//            putExtra("imageUri", currentUri.toString())
//            putExtra("disease_name", fakeResultLabel)
//            putExtra("accuracy", fakeConfidence)
//        }
//        startActivity(intent)
    }

    private fun savePredictionHistory(imageUri: String, label: String, confidenceScore: Float) {
        val predictionHistory = PredictionHistory(imageUri = imageUri, label = label, confidenceScore = confidenceScore)
        val database = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            database.predictionHistoryDao().insert(predictionHistory)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onError(error: String) {
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        results?.firstOrNull()?.categories?.maxByOrNull { it.score }?.let { topCategory ->

            val label: String
            val confidence = topCategory.score
            val formattedConfidence: String

            if (confidence * 100 >= 50) {
                label = topCategory.label
                formattedConfidence = String.format("%.2f", confidence * 100)
            } else {
                label = "Tidak Dikenali"
                formattedConfidence = "N/A"
            }

            Toast.makeText(requireContext(), "Disease: $label, Confidence: $formattedConfidence%", Toast.LENGTH_LONG).show()

            // Save result to history and navigate to ResultActivity
            val currentUri = detectViewModel.currentImageUri.value
            if (currentUri != null) {
                savePredictionHistory(currentUri.toString(), label, confidence)
                val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                    putExtra("imageUri", currentUri.toString())
                    putExtra("disease_name", label)
                    putExtra("accuracy", formattedConfidence)
                }
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
