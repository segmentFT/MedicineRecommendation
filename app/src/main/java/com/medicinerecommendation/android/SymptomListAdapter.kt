package com.medicinerecommendation.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SymptomListAdapter(val symptoms: List<SymptomItem>, val activity: SelectSymptomActivity):
    RecyclerView.Adapter<SymptomListAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val symptomName: TextView = view.findViewById(R.id.symptomName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.symptom_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val displayedSymptomName = symptoms[viewHolder.adapterPosition].name
            Toast.makeText(activity, displayedSymptomName, Toast.LENGTH_SHORT).show()
            activity.findViewById<TextView>(R.id.selectedSymptom).text = displayedSymptomName
            activity.findViewById<Button>(R.id.confirmSymptomButton).isEnabled = true
            activity.selectedSymptom = symptoms[viewHolder.adapterPosition]
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val displayedSymptomName = StringBuilder(symptoms[position].name).toString()

        viewHolder.symptomName.text = if(displayedSymptomName.length > 8) {
            displayedSymptomName.substring(0, 8) + "..."
        }else
            displayedSymptomName
    }

    override fun getItemCount() = symptoms.size

}