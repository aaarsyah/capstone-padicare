package com.example.padicareapp.view.detect

import android.Manifest
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
import com.example.padicareapp.R
import com.example.padicareapp.databinding.FragmentDetectBinding
import com.example.padicareapp.view.camera.CameraActivity
import com.example.padicareapp.view.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.padicareapp.view.result.ResultActivity

class FragmentDetect : Fragment(R.layout.fragment_detect) {

    private lateinit var binding: FragmentDetectBinding
    private var currentImageUri: Uri? = null

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
                showImage()
            } ?: run {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CAMERAX_RESULT) {
                val uri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
                currentImageUri = uri
                showImage()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetectBinding.bind(view)

        val toolbar = binding.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val logoDrawable = resources.getDrawable(R.drawable.logo_tanpatulisan, null)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_tanpatulisan)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false)
        toolbar.logo = BitmapDrawable(resources, scaledBitmap)

        // Image selection and camera
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

    private fun showImage() {
        binding.ivImage.setImageURI(currentImageUri)
    }

    private fun detectDisease() {
        if (currentImageUri == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulate disease detection
        Toast.makeText(requireContext(), "Detecting...", Toast.LENGTH_SHORT).show()

        // Passing the image URI to the ResultActivity
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("imageUri", currentImageUri.toString()) // Send the image URI to ResultActivity
        }
        startActivity(intent)
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
