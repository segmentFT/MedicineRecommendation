package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class SelectRelativeSymptomActivity : AppCompatActivity() {

    lateinit var individualInformation: MutableMap<String, String>
    lateinit var relativeSymptoms: MutableList<SymptomItem>
    lateinit var relativeSymptomListAdapter: RelativeSymptomListAdapter
    lateinit var relativeSymptomList: RecyclerView
    lateinit var selectedRelativeSymptoms: MutableList<SymptomItem>
    lateinit var selectedRelativeSymptomList: RecyclerView
    lateinit var selectedRelativeSymptomListAdapter: SelectedRelativeSymptomListAdapter
    lateinit var isRelativeSymptomItemChecked: MutableList<Boolean>
    lateinit var noMessageText3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_relative_symptom_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        individualInformation = mutableMapOf("gender" to intent.getStringExtra("gender")!!,
            "ageRange" to intent.getStringExtra("ageRange")!!,
            "occupation" to intent.getStringExtra("occupation")!!,
            "position" to intent.getStringExtra("position")!!,
            "mainSymptomName" to intent.getStringExtra("mainSymptomName")!!,
            "mainSymptomId" to intent.getStringExtra("mainSymptomId")!!)

        findViewById<TextView>(R.id.submittedGender).text =
            individualInformation["gender"]!!.split("_")[1]
        findViewById<TextView>(R.id.submittedAgeRange).text =
            individualInformation["ageRange"]!!.split("（")[1].split("）")[0]
        findViewById<TextView>(R.id.submittedOccupation).text = individualInformation["occupation"]
        findViewById<TextView>(R.id.submittedPosition).text = individualInformation["position"]
        findViewById<TextView>(R.id.submittedMainSymptom).text =
            if(individualInformation["mainSymptomName"]!!.length > 9) {
                individualInformation["mainSymptomName"]!!.substring(0, 9) + "..."
            }else{
                individualInformation["mainSymptomName"]
            }

        relativeSymptoms = mutableListOf()
        relativeSymptomList = findViewById(R.id.relativeSymptomList)
        relativeSymptomList.layoutManager = GridLayoutManager(this, 2)
        relativeSymptomListAdapter = RelativeSymptomListAdapter(relativeSymptoms, this)
        relativeSymptomList.adapter = relativeSymptomListAdapter

        noMessageText3 = findViewById(R.id.noMessageText3)
        selectedRelativeSymptoms = mutableListOf()
        selectedRelativeSymptomList = findViewById(R.id.selectedRelativeSymptomList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        selectedRelativeSymptomList.layoutManager = layoutManager
        selectedRelativeSymptomListAdapter = SelectedRelativeSymptomListAdapter(selectedRelativeSymptoms)
        selectedRelativeSymptomList.adapter = selectedRelativeSymptomListAdapter

        isRelativeSymptomItemChecked = mutableListOf()

        sendRequestToSelfDiagnosis()

        findViewById<Button>(R.id.submitSymptomForm).setOnClickListener {
            val intent = Intent(this, SelfDiagnosisActivity::class.java).apply {
                putExtra("sex", convertGenderToUTF8(individualInformation["gender"]!!))
                putExtra("age", convertAgeRangeToUTF8(individualInformation["ageRange"]!!))
                putExtra("occupationId", occupationNameToId(individualInformation["occupation"]!!))
                putExtra("symptomId", convertSymptomToIdSequence())
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SelfDiagnosisActivityCollector.removeActivity(this)
    }

    fun sendRequestToSelfDiagnosis(){
        thread {
            val url = URL("https://jbk.39.net/SelfDiagnosis/DiagnosisHandler.ashx")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:104.0) Gecko/20100101 Firefox/104.0")
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")

            val requestForm = "act=result&" +
                    "sex=${convertGenderToUTF8(individualInformation["gender"]!!)}&" +
                    "age=${convertAgeRangeToUTF8(individualInformation["ageRange"]!!)}&" +
                    "jid=${occupationNameToId(individualInformation["occupation"]!!)}&" +
                    "ids=${individualInformation["mainSymptomId"]}"

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
            displayRelativeSymptomList(responseText.toString())
            connection.disconnect()
        }
    }

    fun displayRelativeSymptomList(responseText: String) {
        val objectArray = JSONObject(responseText).getJSONArray("SList")
        relativeSymptoms.clear()

        for(i in 0 until objectArray.length()){
            val name = objectArray.getJSONObject(i).getString("Name")
            val id = objectArray.getJSONObject(i).getString("ID")
            relativeSymptoms.add(SymptomItem(name, id))
        }

        runOnUiThread {
            val noMessageText2:TextView = findViewById(R.id.noMessageText2)

            if(relativeSymptoms.isEmpty()){
                noMessageText2.visibility = View.VISIBLE
                relativeSymptomList.visibility = View.GONE
            }else{
                noMessageText2.visibility = View.GONE
                relativeSymptomList.visibility = View.VISIBLE
                relativeSymptomListAdapter.notifyDataSetChanged()

                for(i in 0 until relativeSymptoms.size)
                    isRelativeSymptomItemChecked.add(false)
            }
        }
    }

    fun convertSymptomToIdSequence(): String{
        val idSequence = StringBuilder()

        idSequence.append(individualInformation["mainSymptomId"] + ",")
        for(v in selectedRelativeSymptoms)
            idSequence.append(v.id + ",")
        val length = idSequence.toString().length

        return idSequence.toString().substring(0, length - 1)
    }
}