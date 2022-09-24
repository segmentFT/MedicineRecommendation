package com.medicinerecommendation.android

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
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

        finishInstallingButton = findViewById(R.id.finishInstallingButton)
        finishInstallingButton.setOnClickListener {
            finish()
        }

        installingProgressValue = findViewById(R.id.installingProgressValue)
        installingProgressbar = findViewById(R.id.installingProgressbar)

        when(intent.getStringExtra("database")) {
            "disease_database" -> {
                messageText.text = "正在加载疾病数据库,在此期间请勿关闭页面或者让程序进入后台"
                finishInstallingButton.isEnabled = false
                thread {
                    installDiseaseDatabase()
                }
            }
            "medicine_database" -> {
                finishInstallingButton.isEnabled = false
                thread {
                    installMedicineDatabase()
                }
            }
            else -> {}
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

    fun installMedicineDatabase() {
        val dbHelper = MedicineDatabaseOpenHelper(this, "medicine_database.db", 1)
        val db = dbHelper.writableDatabase
        val assetManager = assets

        runOnUiThread {
            messageText.text = "正在加载中药信息，在此期间请勿关闭程序或让程序进入后台"
        }

        for(i in 1..100) {
            val file = assetManager.open("herbs_${i}.json")
            val reader = BufferedReader(InputStreamReader(file))
            val fileContent = StringBuilder()
            reader.use {
                reader.forEachLine {
                    fileContent.append(it)
                }
            }
            loadHerbData(fileContent.toString(), db)

            runOnUiThread {
                installingProgressbar.layoutParams = LinearLayout.LayoutParams(
                    convertDpToPx(this, (i / 100.0 * 320).roundToInt()),
                    convertDpToPx(this, 10))
                installingProgressValue.text = i.toString() + "%"
            }
        }

        runOnUiThread {
            messageText.text = "正在加载中药种类信息，在此期间请勿关闭程序或让程序进入后台"
        }

        var file = assetManager.open("herb_classification.json")
        var reader = BufferedReader(InputStreamReader(file))
        val fileContent = StringBuilder()
        reader.use {
            reader.forEachLine {
                fileContent.append(it)
            }
        }
        loadHerbClassificationData(fileContent.toString(), db)
        fileContent.clear()

        runOnUiThread {
            messageText.text = "正在加载药品成份信息，在此期间请勿关闭程序或让程序进入后台"
        }

        file = assetManager.open("medicines.json")
        reader = BufferedReader(InputStreamReader(file))
        reader.use {
            reader.forEachLine {
                fileContent.append(it)
            }
        }
        loadMedicineIngredientData(fileContent.toString(), db)
        fileContent.clear()

        runOnUiThread {
            messageText.text = "正在加载药品相似度信息，在此期间请勿关闭程序或让程序进入后台"
        }

        file = assetManager.open("similar_medicines.json")
        reader = BufferedReader(InputStreamReader(file))
        reader.use {
            reader.forEachLine {
                fileContent.append(it)
            }
        }
        loadMedicineSimilarityData(fileContent.toString(), db)

        runOnUiThread {
            messageText.text = "药物数据库已经安装完成，可以进行下一步操作"
            finishInstallingButton.isEnabled = true
        }

    }

    fun loadHerbData(fileContent: String, db: SQLiteDatabase) {
        val jsonArray  = JSONArray(fileContent)

        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val herbName = jsonObject.getString("name")
            var alias = jsonObject.getString("alias")
            alias = if(alias == "?") "没有别名信息" else alias
            var ingredient = jsonObject.getString("ingredient")
            ingredient = if(ingredient == "?") "没有成份信息" else ingredient
            var process = jsonObject.getString("process")
            process = if(ingredient == "?") "没有加工采集信息" else process
            var distinguish = jsonObject.getString("distinguish")
            distinguish = if(distinguish == "?") "没有药材鉴别信息" else distinguish
            var pharmacologicalActions = jsonObject.getString("effect")
            pharmacologicalActions =
                if(pharmacologicalActions == "?") "没有药理作用信息" else pharmacologicalActions
            var creationMethod = jsonObject.getString("creation")
            creationMethod = if(creationMethod == "?") "没有药材炮制信息" else creationMethod
            var function = jsonObject.getString("function")
            function = if(function == "?") "没有功能主治信息" else function
            var flavour = jsonObject.getString("taste")
            flavour = if(function == "?") "没有性味信息" else flavour
            var usage = jsonObject.getString("usage")
            usage = if(usage == "?") "没有性味信息" else usage
            var compatibility = jsonObject.getString("compatibility")
            compatibility = if(compatibility == "?") "没有宜忌信息" else compatibility
            var storageMethod = jsonObject.getString("storage")
            storageMethod = if(storageMethod == "?") "没有贮藏方法信息" else storageMethod
            var sideEffects = jsonObject.getString("side_effects")
            sideEffects = if(sideEffects == "?") "没有副作用信息" else sideEffects
            var category = jsonObject.getString("category")
            category = if(category == "?") "没有药物归类信息" else category
            var plant = jsonObject.getString("plant")
            plant = if(plant == "?") "没有药物形态信息" else plant
            var clinicalApplication = jsonObject.getString("application")
            clinicalApplication =
                if(clinicalApplication == "?") "没有临床应用信息" else clinicalApplication
            var positionOfPlant = jsonObject.getString("position")
            positionOfPlant = if(positionOfPlant == "?") "没有药用部位信息" else positionOfPlant
            var sourceBook = jsonObject.getString("source")
            sourceBook = if(sourceBook == "?") "没有来源书籍信息" else sourceBook

            val insertedHerbTuple = ContentValues().apply {
                put("name", herbName)
                put("alias", alias)
                put("ingredient", ingredient)
                put("process", process)
                put("distinguish", distinguish)
                put("effect", pharmacologicalActions)
                put("creation", creationMethod)
                put("function", function)
                put("taste", flavour)
                put("usage", usage)
                put("compatibility", compatibility)
                put("storage", storageMethod)
                put("side_effects", sideEffects)
                put("category", category)
                put("plant", plant)
                put("application", clinicalApplication)
                put("position", positionOfPlant)
                put("source", sourceBook)
            }
            db.insert("herbs", null, insertedHerbTuple)
        }
    }

    fun loadHerbClassificationData(fileContent: String, db: SQLiteDatabase) {
        val jsonArray = JSONArray(fileContent)
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val herbList = jsonObject.getJSONArray("herb")
            val classificationName = jsonObject.getString("classification")

            for(j in 0 until herbList.length()) {
                val insertedClassificationTuple = ContentValues().apply {
                    put("name", herbList.getString(j))
                    put("classification", classificationName)
                }
                db.insert("classifications", null, insertedClassificationTuple)
            }
        }
    }

    fun loadMedicineIngredientData(fileContent: String, db: SQLiteDatabase) {
        val jsonArray = JSONArray(fileContent)
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val medicineName = jsonObject.getString("name")
            val ingredientList = jsonObject.getJSONArray("ingredient")

            if(ingredientList.length() > 0) {
                for(j in 0 until ingredientList.length()) {
                    val insertedIngredientTuple = ContentValues().apply {
                        put("name", medicineName)
                        put("ingredient", ingredientList.getString(j))
                    }
                    db.insert("medicines", null, insertedIngredientTuple)
                }
            }
        }
    }

    fun loadMedicineSimilarityData(fileContent: String, db: SQLiteDatabase) {
        val jsonArray = JSONArray(fileContent)
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val medicineName = jsonObject.getString("medicine_name")
            val similarMedicineList = jsonObject.getJSONArray("similar_medicines")

            if(similarMedicineList.length() > 0) {
                for(j in 0 until similarMedicineList.length()) {
                    val similarMedicine = similarMedicineList.getJSONObject(j)
                    val insertedSimilarMedicineTuple = ContentValues().apply {
                        put("name", medicineName)
                        put("similar_medicine", similarMedicine.getString("name"))
                        put("similarity", similarMedicine.getString("similarity").toFloat())
                    }
                    db.insert(
                        "similarities", null, insertedSimilarMedicineTuple)
                }
            }
        }
    }

}