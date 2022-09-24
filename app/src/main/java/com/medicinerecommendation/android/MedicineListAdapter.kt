package com.medicinerecommendation.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

class MedicineListAdapter(val medicines: MutableList<MedicineItem>, val activity: AppCompatActivity):
    RecyclerView.Adapter<MedicineListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val medicineName: TextView = view.findViewById(R.id.medicineName)
        val generalGrade: TextView = view.findViewById(R.id.generalGrade)
        val commentSource: TextView = view.findViewById(R.id.commentSource)
        val commentSource1: LinearLayout = view.findViewById(R.id.commentSource1)
        val commentSource2: LinearLayout = view.findViewById(R.id.commentSource2)
        val commentSource1Count: TextView = view.findViewById(R.id.commentSource1Count)
        val commentSource2Count: TextView = view.findViewById(R.id.commentSource2Count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.medicine_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, MedicineInformationActivity::class.java)
            intent.putExtra("name", medicines[viewHolder.adapterPosition].name)
            activity.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.medicineName.text = medicines[position].name
        viewHolder.generalGrade.text = "%.2f".format(medicines[position].grade)

        if(medicines[position].grade.absoluteValue < 0.001) {
            viewHolder.commentSource.text = "没有药品的评价信息"
            viewHolder.commentSource1.visibility = View.GONE
            viewHolder.commentSource2.visibility = View.GONE
        } else if (medicines[position].commentCount1 == 0) {
            viewHolder.commentSource.text = "评分由寻医问药网的用户评价计算得到"
            viewHolder.commentSource1.visibility = View.GONE
            viewHolder.commentSource2Count.text = medicines[position].commentCount2.toString()
        } else if (medicines[position].commentCount2 == 0) {
            viewHolder.commentSource.text = "评分由39健康网的用户评分计算得到"
            viewHolder.commentSource2.visibility = View.GONE
            viewHolder.commentSource1Count.text = medicines[position].commentCount1.toString()
        } else {
            viewHolder.commentSource1Count.text = medicines[position].commentCount1.toString()
            viewHolder.commentSource2Count.text = medicines[position].commentCount2.toString()
        }
    }

    override fun getItemCount() = medicines.size

}