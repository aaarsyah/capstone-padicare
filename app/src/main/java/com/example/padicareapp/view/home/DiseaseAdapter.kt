package com.example.padicareapp.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.padicareapp.R

class DiseaseAdapter(
    private val onItemClicked: (Disease) -> Unit
) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    private val diseaseList = mutableListOf<Disease>()

    // Function to submit a new list
    fun submitList(diseases: List<Disease>) {
        diseaseList.clear()
        diseaseList.addAll(diseases)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_penyakit, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        val disease = diseaseList[position]
        holder.bind(disease)
    }

    override fun getItemCount(): Int = diseaseList.size

    inner class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.card_penyakit)
        private val imageDisease: ImageView = itemView.findViewById(R.id.image_penyakit)
        private val textDiseaseName: TextView = itemView.findViewById(R.id.text_nama_penyakit)

        fun bind(disease: Disease) {
            textDiseaseName.text = disease.name

            // Use Glide to load the image from the URL
            Glide.with(itemView.context)
                .load(disease.imageUrl) // Load from the URL provided in the Disease object
                .into(imageDisease)

            // Set click listener for the card
            cardView.setOnClickListener { onItemClicked(disease) }
        }
    }
}
