package com.medicinerecommendation.android

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RelativeSymptomListAdapter(
    val relativeSymptoms: MutableList<SymptomItem>, val activity: SelectRelativeSymptomActivity):
    RecyclerView.Adapter<RelativeSymptomListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val checkRelativeSymptom: CheckBox = view.findViewById(R.id.checkRelativeSymptomName)
        val relativeSymptomName: TextView = view.findViewById(R.id.relativeSymptomName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.relative_symptom_item_layout, parent, false)
        val viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            Toast.makeText(activity, relativeSymptoms[viewHolder.adapterPosition].name, Toast.LENGTH_SHORT).show()
        }

        viewHolder.checkRelativeSymptom.setOnCheckedChangeListener { _, isChecked ->
            activity.isRelativeSymptomItemChecked[viewHolder.adapterPosition] = isChecked

            for(i in 0 until activity.isRelativeSymptomItemChecked.size){
                val isRelativeSymptomContained = activity.selectedRelativeSymptoms.contains(relativeSymptoms[i])

                if(activity.isRelativeSymptomItemChecked[i] && !isRelativeSymptomContained) {
                    activity.selectedRelativeSymptoms.add(relativeSymptoms[i])
                    activity.selectedRelativeSymptomListAdapter.notifyItemInserted(
                        activity.selectedRelativeSymptoms.size - 1)
                } else if(!activity.isRelativeSymptomItemChecked[i] && isRelativeSymptomContained) {
                    val index = activity.selectedRelativeSymptoms.indexOf(relativeSymptoms[i])
                    activity.selectedRelativeSymptoms.removeAt(index)
                    activity.selectedRelativeSymptomListAdapter.notifyItemRemoved(index)
                }
            }

            if(activity.selectedRelativeSymptoms.isEmpty()){
                activity.selectedRelativeSymptomList.visibility = View.GONE
                activity.noMessageText3.visibility = View.VISIBLE
            } else {
                activity.selectedRelativeSymptomList.visibility = View.VISIBLE
                activity.noMessageText3.visibility = View.GONE
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int){
        val relativeSymptomName = relativeSymptoms[position].name
        viewHolder.relativeSymptomName.text = if(relativeSymptomName.length > 9){
            relativeSymptomName.substring(0, 9) + "..."
        }else{
            relativeSymptomName
        }
        viewHolder.checkRelativeSymptom.isChecked = activity.isRelativeSymptomItemChecked[position]
    }

    override fun getItemCount() = relativeSymptoms.size

}