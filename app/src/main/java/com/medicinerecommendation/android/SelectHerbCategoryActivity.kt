package com.medicinerecommendation.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SelectHerbCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_herb_category_layout)

        val herbCategories = mutableListOf("安神药", "拔毒生肌药", "补虚药", "芳香化湿药", "活血化瘀药",
            "解表药", "解毒杀虫药", "开窍药", "理气药", "利水渗湿药", "平喘药", "平肝息风药", "清热药",
            "驱虫药", "祛风湿药", "收涩药", "温里药", "消食药", "泻下药", "涌吐药", "燥湿止痒药", "止咳化痰药",
            "止血药", "其他中草药")
        val herbCategoryList: RecyclerView = findViewById(R.id.herbCategoryList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        herbCategoryList.layoutManager = layoutManager
        herbCategoryList.adapter = HerbCategoryListAdapter(herbCategories, this)
    }
}