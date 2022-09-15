package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DisplayPositionListAdapter(val positions: MutableList<String>, val activity: DisplayPositionsActivity):
    RecyclerView.Adapter<DisplayPositionListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val displayPositionName: TextView = view.findViewById(R.id.displaySelectionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.display_selection_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            when(viewHolder.adapterPosition) {
                0 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.headPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                1 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.neckPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                2 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.chestPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                3 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.abdomenPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                4 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.waistPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                5 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.hipPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                6 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.upperLimbPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                7 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.lowerLimbPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                8 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.bonePositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                10 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.maleReproductionPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                11 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.femaleReproductionPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                12 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.pelvicCavityPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                13 -> {
                    activity.hideNoMessageText()
                    activity.specificPositions.clear()
                    activity.specificPositions.addAll(activity.systemicPositions)
                    activity.specificPositionListAdapter.notifyDataSetChanged()
                }
                else -> {
                    activity.displayNoMessageText()
                    val intent = Intent(activity, DisplayDiseasesActivity::class.java).apply {
                        putExtra("method", "position")
                        putExtra("content", positions[viewHolder.adapterPosition])
                    }
                    activity.startActivity(intent)
                }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.displayPositionName.text = positions[position]
    }

    override fun getItemCount() = positions.size

}