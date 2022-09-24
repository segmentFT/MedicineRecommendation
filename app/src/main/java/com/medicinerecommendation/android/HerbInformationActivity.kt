package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HerbInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.herb_information_layout)

        val herbName = intent.getStringExtra("herbName")!!

        val dbHelper = MedicineDatabaseOpenHelper(this, "medicine_database.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("herbs", null, "name = ?", arrayOf(herbName),
            null, null, null)
        if(cursor.moveToFirst()) {
            findViewById<TextView>(R.id.herbAlias).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("alias")))
            findViewById<TextView>(R.id.herbIngredient).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("ingredient")))
            findViewById<TextView>(R.id.processMethod).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("process")))
            findViewById<TextView>(R.id.distinguishingMethod).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("distinguish")))
            findViewById<TextView>(R.id.pharmacologicalActions).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("effect")))
            findViewById<TextView>(R.id.creationMethod).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("creation")))
            findViewById<TextView>(R.id.herbFunction).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("function")))
            findViewById<TextView>(R.id.flavour).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("taste")))
            findViewById<TextView>(R.id.usage).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("usage")))
            findViewById<TextView>(R.id.compatibility).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("compatibility")))
            findViewById<TextView>(R.id.mattersOfStorage).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("storage")))
            findViewById<TextView>(R.id.sideEffects).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("side_effects")))
            findViewById<TextView>(R.id.category).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("category")))
            findViewById<TextView>(R.id.herbForm).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("plant")))
            findViewById<TextView>(R.id.clinicalApplication).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("application")))
            findViewById<TextView>(R.id.positionOfMedicinalMaterial).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("position")))
            findViewById<TextView>(R.id.sourceBookOfHerb).text =
                cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("source")))
        } else {
            val intent = Intent(this, NoMessageActivity::class.java)
            NoMessageActivity.addActivities(this)
            startActivity(intent)
        }
        cursor.close()
    }
}