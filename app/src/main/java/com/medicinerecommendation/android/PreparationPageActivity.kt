package com.medicinerecommendation.android

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread
import kotlin.math.roundToInt

class PreparationPageActivity : AppCompatActivity() {
    lateinit var installingProgressValue: TextView
    lateinit var installingProgressbar: View
    lateinit var messageText: TextView
    lateinit var finishInstallingButton: Button
    private val diseaseDataFileCount = 100
    private var diseaseDataCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preparation_page_layout)

        messageText = findViewById(R.id.messageText1)
        messageText.text = "正在加载疾病数据库\n在此期间请勿关闭页面或者让程序进入后台"

        finishInstallingButton = findViewById(R.id.finishInstallingButton)
        finishInstallingButton.setOnClickListener {
            finish()
        }
        finishInstallingButton.isEnabled = false

        installingProgressValue = findViewById(R.id.installingProgressValue)
        installingProgressbar = findViewById(R.id.installingProgressbar)

        thread {
            installDiseaseDatabase()
        }
    }

    fun installDiseaseDatabase() {
        val dbHelper = DiseaseDatabaseOpenHelper(this, "disease_database.db", 1)
        val db = dbHelper.writableDatabase
        val assetManager = assets

        for(i in 1..diseaseDataFileCount) {
            val file = assetManager.open("diseases_${i}.json")
            val reader = BufferedReader(InputStreamReader(file))
            val fileContent = StringBuilder()
            reader.use {
                reader.forEachLine {
                    fileContent.append(it)
                }
            }
            loadDataInDiseaseDatabase(fileContent.toString(), db)
        }

        runOnUiThread {
            finishInstallingButton.isEnabled = true
            messageText.text = "疾病数据库已经安装完成，可以进行下一步操作"
        }
    }

    fun loadDataInDiseaseDatabase(fileContent: String, db: SQLiteDatabase) {
        val jsonArray = JSONArray(fileContent)

        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val diseaseInformation = jsonObject.getJSONObject("information")
            val diseaseBasicInformation = diseaseInformation.getJSONObject("basic_information")
            val diagnosisInformation = diseaseInformation.getJSONObject("diagnosis_information")
            val positions = diseaseBasicInformation.getJSONArray("part")
            val departments = diagnosisInformation.getJSONArray("department")
            val chineseTraditionalMedicine =
                diseaseInformation.getJSONArray("chinese_traditional_medicine")

            val diseasesTableTuple = ContentValues()
            diseasesTableTuple.put("disease_name", jsonObject.getString("disease_name"))
            diseasesTableTuple.put("introduction", diseaseInformation.getString("introduction"))
            diseasesTableTuple.put("insurance", diseaseBasicInformation.getString("insurance"))
            diseasesTableTuple.put("alias",
                if(diseaseBasicInformation.getString("alias") == "?") "没有别名"
                else diseaseBasicInformation.getString("alias")
            )
            diseasesTableTuple.put("infection", diseaseBasicInformation.getString("infection"))
            diseasesTableTuple.put("people",
                if(diseaseBasicInformation.getString("people") == "?") "没有易感人群信息"
                else diseaseBasicInformation.getString("people")
            )
            try {
                val symptoms = diseaseBasicInformation.getJSONArray("symptom")
                diseasesTableTuple.put("symptom", jsonArrayToString(symptoms))
            } catch (e: Exception) {
                diseasesTableTuple.put("symptom", "没有症状信息")
            }
            try {
                val complication = diseaseBasicInformation.getJSONArray("complication")
                diseasesTableTuple.put("complication", jsonArrayToString(complication))
            } catch (e: Exception) {
                diseasesTableTuple.put("complication", "没有并发疾病信息")
            }
            diseasesTableTuple.put("cost",
                if(diagnosisInformation.getString("cost") == "?") "没有治疗费用信息"
                else diagnosisInformation.getString("cost")
            )
            diseasesTableTuple.put("ratio",
                if(diagnosisInformation.getString("ratio") == "?") "没有治疗率信息"
                else diagnosisInformation.getString("ratio")
            )
            diseasesTableTuple.put("duration",
                if(diagnosisInformation.getString("duration") == "?") "没有治疗周期信息"
                else diagnosisInformation.getString("duration")
            )
            try {
                val methodsOfCure = diagnosisInformation.getJSONArray("method")
                diseasesTableTuple.put("method", jsonArrayToString(methodsOfCure))
            } catch (e: Exception) {
                diseasesTableTuple.put("method", "没有治疗方法信息")
            }
            try {
                val physicalExamination = diagnosisInformation.getJSONArray("examination")
                diseasesTableTuple.put("examination", jsonArrayToString(physicalExamination))
            } catch (e: Exception) {
                diseasesTableTuple.put("examination", "没有相关检查信息")
            }
            try {
                val medicine = diagnosisInformation.getJSONArray("medicine")
                diseasesTableTuple.put("medicine", jsonArrayToString(medicine))
            } catch (e: Exception) {
                diseasesTableTuple.put("medicine", "没有对症药物信息")
            }
            db.insert("diseases", null, diseasesTableTuple)

            val diseaseName = jsonObject.getString("disease_name")
            loadDataIntoDiseaseToPositionTable(positions, diseaseName, db)
            loadDataIntoDiseaseToDepartmentTable(departments, diseaseName, db)
            loadDataIntoDiseaseToMedicineTable(chineseTraditionalMedicine, diseaseName, db)

            runOnUiThread {
                ++diseaseDataCount
                val progressbarValue = diseaseDataCount / 1500.0
                installingProgressbar.layoutParams = LinearLayout.LayoutParams(
                    convertDpToPx(this, (progressbarValue * 320).roundToInt()),
                    convertDpToPx(this, 10)
                )
                installingProgressValue.text = "%.2f".format(progressbarValue * 100) + "%"
            }
        }
    }

    fun loadDataIntoDiseaseToPositionTable(jsonArray: JSONArray, diseaseName: String, db: SQLiteDatabase) {

        for(i in 0 until jsonArray.length()) {
            val diseaseToPositionTableTuple = ContentValues().apply {
                put("disease_name", diseaseName)
                put("position", jsonArray.getString(i))
            }
            db.insert("disease_to_position", null, diseaseToPositionTableTuple)
        }
    }

    fun loadDataIntoDiseaseToDepartmentTable(jsonArray: JSONArray, diseaseName: String, db: SQLiteDatabase) {
        for(i in 0 until jsonArray.length()) {
            val diseaseToDepartmentTableTuple = ContentValues().apply {
                put("disease_name", diseaseName)
                put("department", jsonArray.getString(i))
            }
            db.insert("disease_to_department", null, diseaseToDepartmentTableTuple)
        }
    }

    fun loadDataIntoDiseaseToMedicineTable(jsonArray: JSONArray, diseaseName: String, db: SQLiteDatabase) {
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val diseaseToMedicineTableTuple = ContentValues().apply {
                put("disease_name", diseaseName)
                put("medicine_name", jsonObject.getString("medicine_name"))
                put("grade", jsonObject.getString("grade").toFloat())
                put("comment_count", jsonObject.getString("total").toInt())
                put("secondary_comment_count", jsonObject.getString("secondary").toInt())
            }
            db.insert("disease_to_medicine", null, diseaseToMedicineTableTuple)
        }
    }

}