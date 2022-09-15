package com.medicinerecommendation.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class PositionListAdapter(val positions: List<String>, val activity: SelectSymptomActivity):
    RecyclerView.Adapter<PositionListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val positionName: TextView = view.findViewById(R.id.positionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.position_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val selectedPosition = positions[viewHolder.adapterPosition]

            if(activity.lastTimeSelectedPosition != selectedPosition){
                activity.messageText.visibility = View.VISIBLE
                activity.displaySymptomsLayout.visibility = View.GONE

                activity.lastTimeSelectedPosition = selectedPosition
                activity.sendRequestToSelfDiagnosis(selectedPosition)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.positionName.text = positions[position]
    }

    override fun getItemCount() = positions.size

}