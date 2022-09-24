package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class NavigationPageActivity : AppCompatActivity() {

    private val diseaseFileCount = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_page_layout)

        val queryBySelfDiagnosis: Button = findViewById(R.id.queryBySelfDiagnosis)
        queryBySelfDiagnosis.setOnClickListener {
            val intent = Intent(this, IndividualConditionsCollectedActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.queryWithPositionButton).setOnClickListener {
            val intent = Intent(this, DisplayPositionsActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.queryWithDepartmentButton).setOnClickListener {
            val intent = Intent(this, DisplayDepartmentsActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.inputDiseaseNameButton).setOnClickListener {
            val inputDiseaseName: EditText = findViewById(R.id.inputDiseaseName)
            if (inputDiseaseName.text.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("错误提示")
                    setMessage("查询框没有输入疾病名")
                    setCancelable(true)
                    setPositiveButton("确定") { _, _ -> }
                    show()
                }
            } else {
                val intent = Intent(this, DisplayDiseasesActivity::class.java).apply {
                    putExtra("method", "direct")
                    putExtra("content", inputDiseaseName.text.toString())
                }
                startActivity(intent)
            }
        }
    }
}