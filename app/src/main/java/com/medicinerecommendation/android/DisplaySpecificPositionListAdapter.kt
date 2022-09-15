package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class DisplaySpecificPositionListAdapter(
    val specificPositions: MutableList<String>, val activity: AppCompatActivity):
    RecyclerView.Adapter<DisplaySpecificPositionListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val displayPositionName: TextView = view.findViewById(R.id.displaySelectionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.display_selection_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, DisplayDiseasesActivity::class.java).apply {
                putExtra("method", "position")
                putExtra("content", specificPositions[viewHolder.adapterPosition])
            }
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.displayPositionName.text = specificPositions[position]
    }

    override fun getItemCount() = specificPositions.size

}