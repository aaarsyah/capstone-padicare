package com.example.padicareapp.view.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

fun File.reduceFileImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path)

    val outputFile = File(this.parent, "compressed_${this.name}")
    val outputStream = FileOutputStream(outputFile)

    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
    outputStream.flush()
    outputStream.close()

    return outputFile
}