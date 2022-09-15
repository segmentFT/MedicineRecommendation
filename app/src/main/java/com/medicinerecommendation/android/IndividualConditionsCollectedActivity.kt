package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IndividualConditionsCollectedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_conditions_collected_layout)
        SelfDiagnosisActivityCollector.addActivity(this)

        val individualInformation =
            mutableMapOf("gender" to "none", "ageRange" to "none", "occupation" to "none")

        val confirmButton: Button = findViewById(R.id.confirmSelection)
        confirmButton.isEnabled = false
        confirmButton.setOnClickListener {
            val intent = Intent(this, SelectSymptomActivity::class.java)
            intent.putExtra("gender", individualInformation["gender"])
            intent.putExtra("ageRange", individualInformation["ageRange"])
            intent.putExtra("occupation", individualInformation["occupation"])
            startActivity(intent)
        }

        val selectOtherOccupationButton: Button = findViewById(R.id.selectOtherOccupationButton)
        selectOtherOccupationButton.setOnClickListener {
            individualInformation["occupation"] = "其他"
            findViewById<TextView>(R.id.selected_occupation).text = "其他"
        }

        val selectGender: RadioGroup = findViewById(R.id.select_gender)
        selectGender.setOnCheckedChangeListener { _, checkedId ->
            individualInformation["gender"] = when(checkedId) {
                R.id.male -> {
                    findViewById<TextView>(R.id.selected_gender).text = "男性"
                    "性别_男"
                }
                R.id.female -> {
                    findViewById<TextView>(R.id.selected_gender).text = "女性"
                    "性别_女"
                }
                else -> "none"
            }

            confirmButton.isEnabled = individualInformation["ageRange"] != "none" &&
                    individualInformation["occupation"] != "none"
        }

        val occupations = listOf("工人", "农民", "教师", "服务员", "司机", "厨师", "办公室职员", "医生",
            "护士", "公务员", "销售", "采购", "其他")
        val occupationList: RecyclerView = findViewById(R.id.occupationSelectedList)
        occupationList.adapter =
            OccupationListAdapter(occupations, this, individualInformation, confirmButton)
        occupationList.layoutManager = GridLayoutManager(this, 3)

        val displaySelectedAgeRange: TextView = findViewById(R.id.selected_age_range)
        val selectAgeRange: RadioGroup = findViewById(R.id.selectAgeRange)
        selectAgeRange.setOnCheckedChangeListener { _, checkedId ->
            individualInformation["ageRange"] = when(checkedId) {
                R.id.lessThanOneMonth -> {
                    displaySelectedAgeRange.text = "新生儿"
                    "疾病年龄_新生儿（一个月内）"
                }
                R.id.twoToElevenMonth -> {
                    displaySelectedAgeRange.text = "婴儿"
                    occupationList.isEnabled = false
                    "疾病年龄_婴儿（2~11个月）"
                }
                R.id.oneToTwelveAge -> {
                    displaySelectedAgeRange.text = "儿童"
                    "疾病年龄_儿童（1~12岁）"
                }
                R.id.thirteenToEighteenAge -> {
                    displaySelectedAgeRange.text = "少年"
                    "疾病年龄_少年（13~18岁）"
                }
                R.id.nineteenToTwentyNineAge -> {
                    displaySelectedAgeRange.text = "青年"
                    "疾病年龄_青年（19~29岁）"
                }
                R.id.thirtyToThirtyNineAge -> {
                    displaySelectedAgeRange.text = "壮年"
                    "疾病年龄_壮年（30~39岁）"
                }
                R.id.fortyToFortyNineAge -> {
                    displaySelectedAgeRange.text = "中年"
                    "疾病年龄_中年（40~49岁）"
                }
                R.id.fiftyToSixtyFourAge -> {
                    displaySelectedAgeRange.text = "中老年"
                    "疾病年龄_中老年（50~64岁）"
                }
                R.id.moreThanSixtyFiveAge -> {
                    displaySelectedAgeRange.text = "老年"
                    "疾病年龄_老年（65以上）"
                }
                else -> "none"
            }

            if(checkedId == R.id.lessThanOneMonth || checkedId == R.id.twoToElevenMonth ||
                checkedId == R.id.oneToTwelveAge || checkedId == R.id.thirteenToEighteenAge) {

                occupationList.visibility = View.GONE
                selectOtherOccupationButton.isEnabled = false
                individualInformation["occupation"] = "其他"
                confirmButton.isEnabled = individualInformation["gender"] != "none"
                findViewById<TextView>(R.id.selected_occupation).text = "其他"
            }else {
                occupationList.visibility = View.VISIBLE
                selectOtherOccupationButton.isEnabled = true
                confirmButton.isEnabled = individualInformation["gender"] != "none" &&
                        individualInformation["occupation"] != "none"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SelfDiagnosisActivityCollector.removeActivity(this)
    }
}