package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class DisplayDiseaseListAdapter(val diseaseNames: MutableList<String>, val activity: AppCompatActivity):
    RecyclerView.Adapter<DisplayDiseaseListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val displayDiseaseName: TextView = view.findViewById(R.id.displayDiseaseName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.display_disease_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val diseaseName = diseaseNames[viewHolder.adapterPosition]
            val intent = Intent(activity, DiseaseInformationActivity::class.java)
            intent.putExtra("diseaseName", diseaseName)
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.displayDiseaseName.text = diseaseNames[position]
    }

    override fun getItemCount() = diseaseNames.size

}