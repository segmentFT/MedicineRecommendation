package com.medicinerecommendation.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MedicineInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.medicine_information_layout)

        val medicineName = intent.getStringExtra("name")!!

        val dbHelper = MedicineDatabaseOpenHelper(this, "medicine_database.db", 1)
        val db = dbHelper.writableDatabase
        var cursor = db.query("medicines", arrayOf("ingredient"), "name = ?",
            arrayOf(medicineName), null, null, null)
        val ingredients: MutableList<String> = mutableListOf()
        if (cursor.moveToFirst()) {
            do {
                ingredients.add(cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("ingredient"))))
            } while (cursor.moveToNext())
        }
        cursor.close()
        if (!ingredients.isEmpty()) {
            findViewById<TextView>(R.id.noMessageText6).visibility = View.GONE
            val medicineIngredientList: RecyclerView = findViewById(R.id.medicineIngredientList)
            medicineIngredientList.layoutManager = GridLayoutManager(this, 2)
            medicineIngredientList.adapter = MedicineIngredientListAdapter(ingredients, this)
            medicineIngredientList.visibility = View.VISIBLE
        }

        cursor = db.query("similarities", null, "name = ?",
            arrayOf(medicineName), null, null, null)
        val similarMedicines: MutableList<SimilarMedicineItem> = mutableListOf()
        if(cursor.moveToFirst()) {
            do {
                val name = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("similar_medicine")))
                val similarity = cursor.getDouble(0.coerceAtLeast(cursor.getColumnIndex("similarity")))
                similarMedicines.add(SimilarMedicineItem(name, similarity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        if(!similarMedicines.isEmpty()) {
            findViewById<TextView>(R.id.noMessageText7).visibility = View.GONE
            val recommendationList: RecyclerView = findViewById(R.id.recommendationList)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recommendationList.layoutManager = layoutManager
            recommendationList.adapter = RecommendationListAdapter(similarMedicines, this)
            recommendationList.visibility = View.VISIBLE
        }
    }
}