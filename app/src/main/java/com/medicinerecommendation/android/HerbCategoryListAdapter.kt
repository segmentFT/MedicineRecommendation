package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class HerbCategoryListAdapter(val herbCategories: MutableList<String>, val activity: AppCompatActivity):
    RecyclerView.Adapter<HerbCategoryListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val displaySelectionName: TextView = view.findViewById(R.id.displaySelectionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.display_selection_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, SelectHerbActivity::class.java).apply {
                putExtra("method", "category")
                putExtra("content", herbCategories[viewHolder.adapterPosition])
            }
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.displaySelectionName.text = herbCategories[position]
    }

    override fun getItemCount() = herbCategories.size

}