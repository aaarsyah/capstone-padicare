package com.example.padicareapp.helper

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.IOException


class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "model_metadata.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError("Failed to initialize Image Classifier: ${e.message}")
            Log.e(TAG, "Image Classifier Error: ${e.message}")
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Load the bitmap from URI (background thread)
                val bitmap = loadBitmapFromUri(imageUri)
                bitmap?.let {
                    // Convert bitmap to TensorImage
                    var tensorImage = TensorImage.fromBitmap(it)

                    // Normalize the TensorImage
//                    tensorImage = normalizeTensorImage(tensorImage)

                    // Run classification
                    val startTime = System.nanoTime()
                    Log.d(TAG, "Starting classification")
                    val results = imageClassifier?.classify(tensorImage)
                    val inferenceTime = (System.nanoTime() - startTime) / 1_000_000

                    Log.d(TAG, "Classification results: $results")
                    Log.d(TAG, "Inference time: $inferenceTime ms")

                    if (results.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            classifierListener?.onError("No classification results returned")
                        }
                        return@launch
                    }

                    // Return results to UI thread
                    withContext(Dispatchers.Main) {
                        classifierListener?.onResults(results, inferenceTime)
                    }
                } ?: run {
                    Log.e(TAG, "Error while classifying: Bitmap is null")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error while classifying: ${e.message}")
            }
        }
    }

    private fun loadBitmapFromUri(imageUri: Uri): Bitmap? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                    decoder.setTargetColorSpace(android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB))
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }

            // Ensure the bitmap is in ARGB_8888 format
            bitmap?.copy(Bitmap.Config.ARGB_8888, true)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to load image from URI", e)
            null
        }
    }


    // Ini karena model nya gaada metadata
//    private fun normalizeTensorImage(tensorImage: TensorImage): TensorImage {
//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)) // Sesuaikan dengan dimensi model
//            .add(NormalizeOp(0.0f, 255.0f)) // Normalisasi ke [0, 1]
//            .build()
//
//        return imageProcessor.process(tensorImage)
//    }
}