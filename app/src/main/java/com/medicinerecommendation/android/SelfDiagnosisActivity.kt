package com.medicinerecommendation.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class SelfDiagnosisActivity : AppCompatActivity() {

    lateinit var diagnoses: MutableList<DiagnosisItem>
    lateinit var diagnosisList: RecyclerView
    lateinit var diagnosisListAdapter: DiagnosisListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.self_diagonsis_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        diagnoses = mutableListOf()
        diagnosisList = findViewById(R.id.diagnosisList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        diagnosisList.layoutManager = layoutManager
        diagnosisListAdapter = DiagnosisListAdapter(diagnoses, this)
        diagnosisList.adapter = diagnosisListAdapter

        findViewById<Button>(R.id.returnToNavigationPageButton).setOnClickListener {
            SelfDiagnosisActivityCollector.returnToNavigationPage()
        }

        sendRequestToSelfDiagnosis()
    }

    override fun onDestroy() {
        super.onDestroy()
        SelfDiagnosisActivityCollector.removeActivity(this)
    }

    fun sendRequestToSelfDiagnosis() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val url = URL("https://jbk.39.net/SelfDiagnosis/DiagnosisHandler.ashx")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8"
                )
                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:104.0) Gecko/20100101 Firefox/104.0"
                )

                val requestForm = "act=result&" +
                        "sex=${intent.getStringExtra("sex")}&" +
                        "age=${intent.getStringExtra("age")}&" +
                        "jid=${intent.getStringExtra("occupationId")}&" +
                        "ids=${intent.getStringExtra("symptomId")}"
                val request = DataOutputStream(connection.outputStream)
                request.use {
                    request.writeBytes(requestForm)
                }

                Log.v("ResponseText", requestForm)

                val responseText = StringBuilder()
                val response = BufferedReader(InputStreamReader(connection.inputStream))
                response.use {
                    response.forEachLine {
                        responseText.append(it)
                    }
                }
                displayDiagnoses(responseText.toString())
            } catch (e: Exception) {
                runOnUiThread {
                    connectionAlertDialog(this).show()
                }
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun displayDiagnoses(responseText: String) {
        val objectArray = JSONObject(responseText).getJSONArray("DList")
        diagnoses.clear()

        for(i in 0 until objectArray.length()) {
            val diagnosis = objectArray.getJSONObject(i)
            val possibility = diagnosis.getString("Score").split(".")[1].substring(0, 4)
            diagnoses.add(DiagnosisItem(diagnosis.getString("Name"),
                possibility.substring(0, 2) + "." + possibility.substring(2, 4)))
        }

        runOnUiThread {
            findViewById<TextView>(R.id.noMessageText4).visibility = View.GONE
            diagnosisList.visibility = View.VISIBLE
            diagnosisListAdapter.notifyDataSetChanged()
        }
    }
}