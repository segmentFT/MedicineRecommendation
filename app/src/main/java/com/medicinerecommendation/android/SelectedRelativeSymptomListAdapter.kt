package com.medicinerecommendation.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectedRelativeSymptomListAdapter(val selectedRelativeSymptoms: MutableList<SymptomItem>):
    RecyclerView.Adapter<SelectedRelativeSymptomListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val selectedRelativeSymptomName: TextView = view.findViewById(R.id.selectedRelativeSymptomName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.selected_relative_symptom_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.selectedRelativeSymptomName.text = selectedRelativeSymptoms[position].name
    }

    override fun getItemCount() = selectedRelativeSymptoms.size

}