package com.medicinerecommendation.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class OccupationListAdapter(val occupations: List<String>, val activity: AppCompatActivity,
                            val individualInformation: MutableMap<String, String>, val confirmButton: Button):
    RecyclerView.Adapter<OccupationListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val occupationName: TextView = view.findViewById(R.id.occupationName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.occupation_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            activity.findViewById<TextView>(R.id.selected_occupation).text =
                occupations[viewHolder.adapterPosition]
            individualInformation["occupation"] = occupations[viewHolder.adapterPosition]
            confirmButton.isEnabled =
                individualInformation["gender"] != "none" && individualInformation["ageRange"] != "none"
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.occupationName.text = occupations[position]
    }

    override fun getItemCount() = occupations.size

}