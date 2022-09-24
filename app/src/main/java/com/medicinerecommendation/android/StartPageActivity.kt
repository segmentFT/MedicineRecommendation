package com.medicinerecommendation.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class StartPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page_layout)

        findViewById<Button>(R.id.openNavigationPageButton).setOnClickListener {
            val intent = Intent(this, NavigationPageActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.openHerbCategoryPageButton).setOnClickListener {
            val intent = Intent(this, SelectHerbCategoryActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.queryHerbWithInputName).setOnClickListener {
            val herbName = findViewById<EditText>(R.id.inputHerbName).text
            if(herbName.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("没有输入信息")
                    setMessage("需要先在输入框中输入中药材名称，之后才能进行查询")
                    setCancelable(true)
                    setPositiveButton("确定") { _, _ -> }
                    show()
                }
            } else {
                val intent = Intent(this, SelectHerbActivity::class.java)
                intent.putExtra("method", "input")
                intent.putExtra("content", herbName.toString())
                startActivity(intent)
            }
        }

        var databaseFile = getDatabasePath("disease_database.db")
        if (!databaseFile.exists()) {
            val intent = Intent(this, PreparationPageActivity::class.java)
            intent.putExtra("database", "disease_database")
            AlertDialog.Builder(this).apply {
                setTitle("未安装疾病数据库")
                setMessage("没有查找到疾病数据库，点击确定按钮后将开始安装疾病数据库")
                setCancelable(false)
                setPositiveButton("确定") { _, _ ->
                    startActivity(intent)
                }
                show()
            }
        }
        databaseFile = getDatabasePath("medicine_database.db")
        if(!databaseFile.exists()) {
            val intent = Intent(this, PreparationPageActivity::class.java)
            intent.putExtra("database", "medicine_database")
            AlertDialog.Builder(this).apply {
                setTitle("未安装药物数据库")
                setMessage("没有查找到药物数据库，点击确定按钮后将开始安装药物数据库")
                setCancelable(false)
                setPositiveButton("确定") { _, _ ->
                    startActivity(intent)
                }
                show()
            }
        }
    }
}