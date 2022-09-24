package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class RecommendationListAdapter(val similarMedicines: MutableList<SimilarMedicineItem>,
                                val activity: AppCompatActivity):
    RecyclerView.Adapter<RecommendationListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val recommendedMedicineName: TextView = view.findViewById(R.id.recommendedMedicineName)
        val medicineSimilarity: TextView = view.findViewById(R.id.medicineSimilarity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recommendation_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, MedicineInformationActivity::class.java)
            intent.putExtra("name", similarMedicines[viewHolder.adapterPosition].name)
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.recommendedMedicineName.text = similarMedicines[position].name
        viewHolder.medicineSimilarity.text = "%.6f".format(similarMedicines[position].similarity)
    }

    override fun getItemCount() = similarMedicines.size

}