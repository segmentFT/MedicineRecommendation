package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class QueriedHerbListAdapter(val queriedHerbs: MutableList<String>, val activity: AppCompatActivity):
    RecyclerView.Adapter<QueriedHerbListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val queriedHerbName: TextView = view.findViewById(R.id.queriedHerbName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.queried_herb_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, HerbInformationActivity::class.java)
            intent.putExtra("herbName", queriedHerbs[viewHolder.adapterPosition])
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.queriedHerbName.text = queriedHerbs[position]
    }

    override fun getItemCount() = queriedHerbs.size

}