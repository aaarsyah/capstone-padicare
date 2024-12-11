//package com.example.padicareapp.helper
//
//import android.content.Context
//import android.graphics.Bitmap
//import com.example.padicareapp.view.detect.FragmentDetect
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import org.tensorflow.lite.support.image.TensorImage
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//class AutoEncoderHelper(
//    context: Context,
//    modelPath: String,
//    private val inputSize: Int = 256, // Ukuran input 256x256 untuk model
//    private val threshold: Float = 0.006f // Threshold untuk validasi
//) {
//    private var interpreter: Interpreter
//
//    init {
//        val assetFileDescriptor = context.assets.openFd(modelPath)
//        val inputStream = assetFileDescriptor.createInputStream()
//        val modelBytes = inputStream.readBytes()
//        inputStream.close()
//
//        val byteBuffer = ByteBuffer.allocateDirect(modelBytes.size).apply {
//            order(ByteOrder.nativeOrder())
//            put(modelBytes)
//        }
//
//        val options = Interpreter.Options()
//        try {
//            val flexDelegate = org.tensorflow.lite.flex.FlexDelegate()
//            options.addDelegate(flexDelegate)
//        } catch (e: Exception) {
//            throw RuntimeException("Failed to load TensorFlow Flex Delegate. Make sure the dependency is added.", e)
//        }
//
//        interpreter = Interpreter(byteBuffer, options)
//        val outputIndex = 0
//        val outputShape = interpreter.getOutputTensor(outputIndex).shape()
//        val outputDataType = interpreter.getOutputTensor(outputIndex).dataType()
//
//        android.util.Log.d("TFLite", "Output Shape: ${outputShape.contentToString()}")
//        android.util.Log.d("TFLite", "Output DataType: $outputDataType")
//    }
//
//    fun validateImage(inputBitmap: Bitmap): Boolean {
//        // Resize input bitmap to match model's expected input size
//        val resizedBitmap = Bitmap.createScaledBitmap(inputBitmap, inputSize, inputSize, true)
//
//        // Convert Bitmap to TensorImage
//        val tensorImage = TensorImage(DataType.FLOAT32)
//        tensorImage.load(resizedBitmap)
//
//        // Prepare input buffer
//        val inputBuffer = tensorImage.buffer
//
//        // Prepare output buffer
//        val outputShape = interpreter.getOutputTensor(0).shape() // [1, ..., ...]
//
//        // Hitung buffer size dengan benar (256 * 256 * 3 * 4 bytes)
//        val outputBufferSize = outputShape[1] * outputShape[2] * outputShape[3] * 4 // FLOAT32 = 4 bytes
//        val outputBuffer = ByteBuffer.allocateDirect(outputBufferSize).apply {
//            order(ByteOrder.nativeOrder())
//        }
//
//        android.util.Log.d("TFLite EE", "Output Shape: ${outputShape.contentToString()}")
//        android.util.Log.d("TFLite EE", "Output Buffer Size: $outputBufferSize")
//
//        // Run inference
//        interpreter.run(inputBuffer, outputBuffer)
//
//        // Calculate reconstruction error
//        val reconstructedBuffer = outputBuffer.asFloatBuffer()
//        val originalBuffer = inputBuffer.asFloatBuffer()
//
//        var error = 0f
//        while (originalBuffer.hasRemaining() && reconstructedBuffer.hasRemaining()) {
//            val original = originalBuffer.get()
//            val reconstructed = reconstructedBuffer.get()
//            error += (original - reconstructed) * (original - reconstructed)
//        }
//
//        // Compare error with the threshold
//        return error <= threshold // Return true if image is valid
//    }
//
//    fun close() {
//        interpreter.close()
//    }
//}
