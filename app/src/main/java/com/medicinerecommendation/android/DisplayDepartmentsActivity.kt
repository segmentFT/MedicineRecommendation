package com.medicinerecommendation.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DisplayDepartmentsActivity : AppCompatActivity() {

    lateinit var noMessageText: TextView
    lateinit var displaySelectionsTitle: TextView
    lateinit var departmentList: RecyclerView
    lateinit var specificDepartmentList: RecyclerView
    lateinit var departments: MutableList<String>
    lateinit var specificDepartments: MutableList<String>
    lateinit var internalMedicine: MutableList<String>
    lateinit var surgery: MutableList<String>
    lateinit var gynaecology: MutableList<String>
    lateinit var facialFeatures: MutableList<String>
    lateinit var dermatoloty: MutableList<String>
    lateinit var chineseMedicine: MutableList<String>
    lateinit var hepatopathy: MutableList<String>
    lateinit var psychiatric: MutableList<String>
    lateinit var specificDepartmentListAdapter: DisplaySpecificDepartmentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_selections_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        noMessageText = findViewById(R.id.noMessageText5)
        noMessageText.text = "请选择大致的科室分类"
        displaySelectionsTitle = findViewById(R.id.displaySelectionsTitle)
        displaySelectionsTitle.text = "选择疾病的对应科室"

        departments = mutableListOf("内科", "外科", "儿科", "妇产科", "男科", "五官科", "皮肤性病", "生殖健康",
            "中西医结合科", "肝病", "精神心理科", "肿瘤科", "传染科", "老年科", "体检保健课", "成瘾医学科", "核医学科",
            "急诊科", "营养科")
        departmentList = findViewById(R.id.generalSelectionList)
        departmentList.layoutManager = GridLayoutManager(this, 2)
        departmentList.adapter = DisplayDepartmentListAdapter(departments, this)

        internalMedicine = mutableListOf("呼吸内科", "消化内科", "神经内科", "内分泌科", "肾内科", "风湿科",
            "血液科", "心血管内科")
        surgery = mutableListOf("普外科", "心胸外科", "肝胆外科", "胃肠外科", "脑外科", "泌尿外科", "骨科", "肛肠外科",
            "乳腺外科", "血液外科", "器官移植", "烧伤科", "手外科", "外伤科")
        gynaecology = mutableListOf("妇科", "产科")
        facialFeatures = mutableListOf("眼科", "耳鼻喉科", "口腔科")
        dermatoloty = mutableListOf("皮肤科", "性病科")
        chineseMedicine = mutableListOf("中医科")
        hepatopathy = mutableListOf("肝炎")
        psychiatric = mutableListOf("精神病科", "心理咨询")

        specificDepartments = mutableListOf()
        specificDepartmentList = findViewById(R.id.specificSelectionList)
        specificDepartmentListAdapter = DisplaySpecificDepartmentListAdapter(specificDepartments, this)
        specificDepartmentList.layoutManager = GridLayoutManager(this, 2)
        specificDepartmentList.adapter = specificDepartmentListAdapter
    }

    fun showNoMessageText() {
        noMessageText.visibility = View.VISIBLE
        specificDepartmentList.visibility = View.GONE
    }

    fun hideNoMessageText() {
        noMessageText.visibility = View.GONE
        specificDepartmentList.visibility = View.VISIBLE
    }

}