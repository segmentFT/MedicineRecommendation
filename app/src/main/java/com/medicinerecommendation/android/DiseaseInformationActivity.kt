package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DiseaseInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.disease_information_layout)

        val diseaseName = intent.getStringExtra("diseaseName")!!
        val dbHelper = DiseaseDatabaseOpenHelper(this, "disease_database.db", 1)
        val db = dbHelper.writableDatabase
        var cursor = db.query("diseases", null, "disease_name = ?", arrayOf(diseaseName),
            null, null, null)
        if(!cursor.moveToFirst()) {
            NoMessageActivity.addActivities(this)
            val intent = Intent(this, NoMessageActivity::class.java)
            startActivity(intent)
        } else {
            val introduction: TextView = findViewById(R.id.diseaseIntroductionContent)
            val insurance: TextView = findViewById(R.id.insuranceContent)
            val alias: TextView = findViewById(R.id.aliasContent)
            val infection: TextView = findViewById(R.id.infectionContent)
            val susceptiblePopulation: TextView = findViewById(R.id.susceptiblePopulationContent)
            val relativeSymptom: TextView = findViewById(R.id.relativeSymptomContent)
            val complication: TextView = findViewById(R.id.complicationContent)
            val costOfCure: TextView = findViewById(R.id.costOfCureContent)
            val ratioOfCure: TextView = findViewById(R.id.ratioOfCureContent)
            val treatmentDuration: TextView = findViewById(R.id.treatmentDurationContent)
            val treatmentMethods: TextView = findViewById(R.id.treatmentMethodContent)
            val relativeExamination: TextView = findViewById(R.id.relativeExaminationContent)
            val commonMedicine: TextView = findViewById(R.id.commonMedicineContent)

            introduction.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("introduction")))
            insurance.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("insurance")))
            alias.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("alias")))
            infection.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("infection")))
            susceptiblePopulation.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("people")))
            relativeSymptom.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("symptom")))
            complication.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("complication")))
            costOfCure.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("cost")))
            ratioOfCure.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("ratio")))
            treatmentDuration.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("duration")))
            treatmentMethods.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("method")))
            relativeExamination.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("examination")))
            commonMedicine.text = cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("medicine")))
        }
        cursor.close()

        cursor = db.query("disease_to_position", arrayOf("position"), "disease_name = ?",
            arrayOf(diseaseName), null, null, null)
        if(cursor.moveToFirst()) {
            val position: TextView = findViewById(R.id.positionContent)
            val positionContent = StringBuilder()

            do {
                positionContent.append(cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("position"))))
            } while(cursor.moveToNext())
            position.text = stringBuilderToString(positionContent)
        }
        cursor.close()

        cursor = db.query("disease_to_department", arrayOf("department"), "disease_name = ?",
            arrayOf(diseaseName), null, null, null)
        if(cursor.moveToFirst()) {
            val department: TextView = findViewById(R.id.departmentContent)
            val departmentContent = StringBuilder()

            do {
                departmentContent.append(cursor.getString(0.coerceAtLeast(cursor.getColumnIndex("department"))))
            } while(cursor.moveToNext())
            department.text = stringBuilderToString(departmentContent)
        }
        cursor.close()
        db.close()

        findViewById<Button>(R.id.displayMedicineButton).setOnClickListener {
            val intent = Intent(this, DisplayMedicineActivity::class.java)
            intent.putExtra("diseaseName", diseaseName)
            startActivity(intent)
        }
        findViewById<Button>(R.id.redirectToNavigationPage).setOnClickListener {
            SelfDiagnosisActivityCollector.returnToNavigationPage()
            finish()
        }
    }
}