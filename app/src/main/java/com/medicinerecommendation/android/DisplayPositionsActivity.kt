package com.medicinerecommendation.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DisplayPositionsActivity : AppCompatActivity() {

    lateinit var positions: MutableList<String>
    lateinit var headPositions: MutableList<String>
    lateinit var neckPositions: MutableList<String>
    lateinit var chestPositions: MutableList<String>
    lateinit var abdomenPositions: MutableList<String>
    lateinit var waistPositions: MutableList<String>
    lateinit var hipPositions: MutableList<String>
    lateinit var upperLimbPositions: MutableList<String>
    lateinit var lowerLimbPositions: MutableList<String>
    lateinit var bonePositions: MutableList<String>
    lateinit var maleReproductionPositions: MutableList<String>
    lateinit var femaleReproductionPositions: MutableList<String>
    lateinit var pelvicCavityPositions: MutableList<String>
    lateinit var systemicPositions: MutableList<String>
    lateinit var specificPositions: MutableList<String>
    lateinit var specificPositionList: RecyclerView
    lateinit var specificPositionListAdapter: DisplaySpecificPositionListAdapter
    lateinit var noMessageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_selections_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        positions = mutableListOf("头部", "颈部", "胸部", "腹部", "腰部", "臀部", "上肢", "下肢", "骨", "会阴部",
            "男性生殖", "女性生殖", "盆腔", "全身", "心理", "背部", "其他")
        headPositions = mutableListOf("鼻", "耳", "口", "颅脑", "面部", "咽喉", "眼")
        neckPositions = mutableListOf("甲状腺", "气管")
        chestPositions = mutableListOf("肺", "膈肌", "乳房", "食管", "心脏", "纵隔")
        abdomenPositions = mutableListOf("肠", "肝", "胆", "腹膜", "肠系膜", "阑尾", "脾", "胃", "胰腺")
        waistPositions = mutableListOf("肾", "肾上腺", "输尿管")
        hipPositions = mutableListOf("肛门")
        upperLimbPositions = mutableListOf("肩部", "前臂", "上臂", "手部", "肘部")
        lowerLimbPositions = mutableListOf("大腿", "膝部", "小腿", "足部")
        bonePositions = mutableListOf("骨髓", "关节", "脊髓", "脊柱", "肋骨", "颅骨", "盆骨", "其他骨", "上肢骨", "下肢骨")
        maleReproductionPositions = mutableListOf("睾丸", "前列腺", "阴囊", "阴茎", "输精管")
        femaleReproductionPositions = mutableListOf("卵巢", "输卵管", "外阴", "阴道", "子宫")
        pelvicCavityPositions = mutableListOf("膀胱", "尿道")
        systemicPositions = mutableListOf("肌肉", "淋巴", "血液血管", "免疫系统", "皮肤", "周围神经系统")

        noMessageText = findViewById(R.id.noMessageText5)

        val generalPositionList: RecyclerView = findViewById(R.id.generalSelectionList)
        generalPositionList.layoutManager = GridLayoutManager(this, 2)
        generalPositionList.adapter = DisplayPositionListAdapter(positions, this)

        specificPositions = mutableListOf()
        specificPositionList = findViewById(R.id.specificSelectionList)
        specificPositionList.layoutManager = GridLayoutManager(this, 2)
        specificPositionListAdapter = DisplaySpecificPositionListAdapter(specificPositions, this)
        specificPositionList.adapter = specificPositionListAdapter
    }

    fun displayNoMessageText() {
        noMessageText.visibility = View.VISIBLE
        specificPositionList.visibility = View.GONE
    }

    fun hideNoMessageText() {
        noMessageText.visibility = View.GONE
        specificPositionList.visibility = View.VISIBLE
    }
}