package com.medicinerecommendation.android

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SelectHerbActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_herb_layout)

        val queryMethod = intent.getStringExtra("method")!!
        val queryContent = intent.getStringExtra("content")!!
        val dbHelper = MedicineDatabaseOpenHelper(this, "medicine_database.db", 1)
        val db = dbHelper.writableDatabase

        lateinit var cursor: Cursor
        if(queryMethod == "category") {
            cursor = db.query("classifications", null, "classification = ?",
                arrayOf(queryContent), null, null, null)
        }else if(queryMethod == "input") {
            cursor = db.query("herbs", arrayOf("name"), "name like ?",
                arrayOf("%${queryContent}%"), null, null, null)
        }

        val queriedHerbs: MutableList<String> = mutableListOf()
        if(cursor.moveToFirst()) {
            do {
                queriedHerbs.add(cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("name"))))
            } while(cursor.moveToNext())
        } else {
            val intent = Intent(this, NoMessageActivity::class.java)
            NoMessageActivity.addActivities(this)
            startActivity(intent)
        }
        cursor.close()

        val queriedHerbList: RecyclerView = findViewById(R.id.queriedHerbList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        queriedHerbList.layoutManager = layoutManager
        queriedHerbList.adapter = QueriedHerbListAdapter(queriedHerbs, this)
    }
}