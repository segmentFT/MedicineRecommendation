package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class SelectSymptomActivity : AppCompatActivity() {

    lateinit var individualInformation: Map<String, String>
    lateinit var positions: MutableList<String>
    lateinit var symptoms: MutableList<SymptomItem>
    lateinit var messageText: TextView
    lateinit var displaySymptomsLayout: RelativeLayout
    lateinit var symptomListAdapter: SymptomListAdapter
    var lastTimeSelectedPosition: String? = null
    var selectedSymptom: SymptomItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_symptom_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        positions = mutableListOf("头部", "胸部", "上肢", "腹部", "下肢", "颈部", "背部", "骨", "全身", "腰部", "盆腔", "臀部")
        individualInformation = mapOf("gender" to (intent.getStringExtra("gender") ?: ""),
            "ageRange" to (intent.getStringExtra("ageRange") ?: ""),
            "occupation" to (intent.getStringExtra("occupation") ?: ""))
        positions.add(if(individualInformation["gender"] == "性别_男") "男性生殖" else "女性生殖")

        messageText = findViewById(R.id.noMessageText1)
        displaySymptomsLayout = findViewById(R.id.displaySymptomsLayout)

        val positionList: RecyclerView = findViewById(R.id.positionList)
        val positionListLayout = LinearLayoutManager(this)
        positionListLayout.orientation = LinearLayoutManager.VERTICAL
        positionList.layoutManager = positionListLayout
        positionList.adapter = PositionListAdapter(positions, this)

        symptoms = mutableListOf()
        val symptomList: RecyclerView = findViewById(R.id.symptomList)
        symptomList.layoutManager = GridLayoutManager(this, 3)
        symptomListAdapter = SymptomListAdapter(symptoms, this)
        symptomList.adapter = symptomListAdapter

        findViewById<Button>(R.id.confirmSymptomButton).setOnClickListener {
            val intent = Intent(this, SelectRelativeSymptomActivity::class.java).apply {
                putExtra("gender", individualInformation["gender"])
                putExtra("ageRange", individualInformation["ageRange"])
                putExtra("occupation", individualInformation["occupation"])
                putExtra("position", lastTimeSelectedPosition)
                putExtra("mainSymptomName", selectedSymptom?.name)
                putExtra("mainSymptomId", selectedSymptom?.id)
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SelfDiagnosisActivityCollector.removeActivity(this)
    }

    fun sendRequestToSelfDiagnosis(position: String) {
        thread {
            var connection: HttpURLConnection? = null

            try {
                val url = URL("https://jbk.39.net/SelfDiagnosis/DiagnosisHandler.ashx")
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8"
                )
                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:104.0) Gecko/20100101 Firefox/104.0"
                )

                val requestForm = "act=body&" +
                        "q=${convertPositionToUTF8(position)}&" +
                        "sex=${convertGenderToUTF8(individualInformation["gender"]!!)}&" +
                        "age=${convertAgeRangeToUTF8(individualInformation["ageRange"]!!)}&" +
                        "jid=${occupationNameToId(individualInformation["occupation"]!!)}"

                val request = DataOutputStream(connection.outputStream)
                request.use {
                    request.writeBytes(requestForm)
                }

                val response = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = StringBuilder()
                response.use {
                    response.forEachLine {
                        responseText.append(it)
                    }
                }
                displaySymptomList(responseText.toString())
            } catch (e: Exception) {
                runOnUiThread {
                    connectionAlertDialog(this).show()
                }
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun displaySymptomList(responseText: String) {
        symptoms.clear()

        val dataList = JSONObject(responseText).getJSONArray("data")
        for(i in 0 until dataList.length()){
            val symptomName = dataList.getJSONObject(i).getString("Value")
            val symptomId = dataList.getJSONObject(i).getString("Key")
            symptoms.add(SymptomItem(symptomName, symptomId))
        }

        runOnUiThread {
            messageText.visibility = View.GONE
            displaySymptomsLayout.visibility = View.VISIBLE
            symptomListAdapter.notifyDataSetChanged()
        }
    }
}