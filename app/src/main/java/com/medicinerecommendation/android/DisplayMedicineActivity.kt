package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DisplayMedicineActivity : AppCompatActivity() {

    lateinit var medicines: MutableList<MedicineItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_medicine_layout)

        val diseaseName = intent.getStringExtra("diseaseName")!!

        medicines = mutableListOf()
        val medicineList: RecyclerView = findViewById(R.id.medicineList)
        val medicineListAdapter: MedicineListAdapter = MedicineListAdapter(medicines, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        medicineList.layoutManager = layoutManager
        medicineList.adapter = medicineListAdapter

        val dbHelper =
            DiseaseDatabaseOpenHelper(this, "disease_database.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("disease_to_medicine", null, "disease_name = ?",
            arrayOf(diseaseName), null, null, null)
        if(!cursor.moveToFirst()){
            val intent = Intent(this, NoMessageActivity::class.java)
            NoMessageActivity.addActivities(this)
            startActivity(intent)
        }else{
            do {
                val medicineName = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("medicine_name")))
                val generalGrade = cursor.getDouble(0.coerceAtLeast(cursor.getColumnIndex("grade")))
                val commentSource1Count = cursor.getInt(0.coerceAtLeast(cursor.getColumnIndex("comment_count")))
                val commentSource2Count =
                    cursor.getInt(0.coerceAtLeast(cursor.getColumnIndex("secondary_comment_count")))
                medicines.add(MedicineItem(medicineName, generalGrade, commentSource1Count, commentSource2Count))
            } while(cursor.moveToNext())
            medicineListAdapter.notifyDataSetChanged()
        }
        db.close()

        findViewById<Button>(R.id.sortByGradeButton).setOnClickListener {
            medicines.sortWith(Comparator { medicine1, medicine2 ->
                ((medicine1.grade - medicine2.grade) * 10).toInt()
            })
            medicineListAdapter.notifyDataSetChanged()
        }
        findViewById<Button>(R.id.sortByCommentCountButton).setOnClickListener {
            medicines.sortWith(Comparator { medicine1, medicine2 ->
                (medicine1.commentCount1 + medicine1.commentCount2) -
                        (medicine2.commentCount1 + medicine2.commentCount2)
            })
            medicineListAdapter.notifyDataSetChanged()
        }
    }
}