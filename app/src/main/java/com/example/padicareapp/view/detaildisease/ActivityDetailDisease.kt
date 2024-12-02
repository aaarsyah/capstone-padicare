package com.example.padicareapp.view.detaildisease

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.padicareapp.R

class ActivityDetailDisease : AppCompatActivity() {

    private lateinit var resultImage: ImageView
    private lateinit var resultText: TextView
    private lateinit var diseaseDescription: TextView
    private lateinit var preventionTips: TextView
    private lateinit var treatmentTips: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_disease)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.detail_penyakit) // Judul toolbar
            setDisplayHomeAsUpEnabled(true) // Menambahkan tombol back
        }

        // Initialize views
        resultImage = findViewById(R.id.result_image)
        resultText = findViewById(R.id.result_text)
        diseaseDescription = findViewById(R.id.disease_description)
        preventionTips = findViewById(R.id.pencegahan_tips)
        treatmentTips = findViewById(R.id.prevention_tips)

        // Ambil data dari Intent
        val diseaseId = intent.getStringExtra("diseaseId")
        val diseaseName = intent.getStringExtra("diseaseName")
        val diseaseImage = intent.getStringExtra("diseaseImage")
        val diseaseDesc = intent.getStringExtra("diseaseDesc")
        val diseasePencegahan = intent.getStringExtra("diseasePencegahan")
        val diseasePengobatan = intent.getStringExtra("diseasePengobatan")

        // Menampilkan data pada UI
        resultText.text = diseaseName
        Glide.with(this)
            .load(diseaseImage)
            .into(resultImage)

        // Set the additional data
        diseaseDescription.text = "$diseaseDesc"
        preventionTips.text = "$diseasePencegahan"
        treatmentTips.text = "$diseasePengobatan"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
