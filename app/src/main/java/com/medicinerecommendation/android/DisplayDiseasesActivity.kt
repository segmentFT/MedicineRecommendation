package com.medicinerecommendation.android

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DisplayDiseasesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_diseases_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        val queryMethod = intent.getStringExtra("method")!!
        val queryContent = intent.getStringExtra("content")!!
        val dbHelper = DiseaseDatabaseOpenHelper(this, "disease_database.db", 1)
        val db = dbHelper.writableDatabase

        val diseaseNames = mutableListOf<String>()
        val displayDiseaseList: RecyclerView = findViewById(R.id.displayDiseaseList)
        val displayDiseaseListAdapter = DisplayDiseaseListAdapter(diseaseNames, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        displayDiseaseList.layoutManager = layoutManager
        displayDiseaseList.adapter = displayDiseaseListAdapter

        lateinit var cursor: Cursor
        if(queryMethod == "position") {
            cursor = db.query(
                "disease_to_position", arrayOf("disease_name"),
                "position = ?", arrayOf(queryContent), null, null, null
            )
        } else if (queryMethod == "department") {
            cursor = db.query("disease_to_department", arrayOf("disease_name"),
                "department = ?", arrayOf(queryContent), null, null, null)
        } else if (queryMethod == "direct") {
            cursor = db.query("diseases", arrayOf("disease_name"), "disease_name like ?",
                arrayOf("%${queryContent}%"), null, null, null)
        }

        if(!cursor.moveToFirst()) {
            val intent = Intent(this, NoMessageActivity::class.java)
            NoMessageActivity.addActivities(this)
            startActivity(intent)
        } else {
            diseaseNames.clear()
            do {
                diseaseNames.add(cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("disease_name"))))
            } while (cursor.moveToNext())
            displayDiseaseListAdapter.notifyDataSetChanged()
        }
        cursor.close()
        db.close()
    }
}