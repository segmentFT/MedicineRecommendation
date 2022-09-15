package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DiagnosisListAdapter(val diagnoses: MutableList<DiagnosisItem>, val activity: SelfDiagnosisActivity):
    RecyclerView.Adapter<DiagnosisListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val diseaseName: TextView = view.findViewById(R.id.diseaseName)
        val possibilityValue: TextView = view.findViewById(R.id.possibilityValue)
        val possibilityProgressbar: View = view.findViewById(R.id.possibilityProgressbar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.diagnosis_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, DiseaseInformationActivity::class.java).apply {
                putExtra("diseaseName", diagnoses[viewHolder.adapterPosition].name)
            }
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.diseaseName.text = diagnoses[position].name
        val possibility = diagnoses[position].possibility
        viewHolder.possibilityValue.text = possibility + "%"
        viewHolder.possibilityProgressbar.layoutParams =
            LinearLayout.LayoutParams(
                convertDpToPx(activity, possibility.split(".")[0].toInt() * 2),
                convertDpToPx(activity, 10))
    }

    override fun getItemCount() = diagnoses.size

}