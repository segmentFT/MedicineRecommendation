package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DisplayDepartmentListAdapter(val departments: MutableList<String>, val activity: DisplayDepartmentsActivity):
    RecyclerView.Adapter<DisplayDepartmentListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val generalDepartment: TextView = view.findViewById(R.id.displaySelectionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.display_selection_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            when(viewHolder.adapterPosition) {
                0 -> showSpecificDepartments(activity.internalMedicine)
                1 -> showSpecificDepartments(activity.surgery)
                3 -> showSpecificDepartments(activity.gynaecology)
                5 -> showSpecificDepartments(activity.facialFeatures)
                6 -> showSpecificDepartments(activity.dermatoloty)
                8 -> showSpecificDepartments(activity.chineseMedicine)
                9 -> showSpecificDepartments(activity.hepatopathy)
                10 -> showSpecificDepartments(activity.psychiatric)
                else -> {
                    activity.showNoMessageText()
                    val intent = Intent(activity, DisplayDiseasesActivity::class.java).apply {
                        putExtra("method", "department")
                        putExtra("content", departments[viewHolder.adapterPosition])
                    }
                    activity.startActivity(intent)
                }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.generalDepartment.text = departments[position]
    }

    override fun getItemCount() = departments.size

    fun showSpecificDepartments(content: MutableList<String>) {
        activity.hideNoMessageText()
        activity.specificDepartments.clear()
        activity.specificDepartments.addAll(content)
        activity.specificDepartmentListAdapter.notifyDataSetChanged()
    }

}