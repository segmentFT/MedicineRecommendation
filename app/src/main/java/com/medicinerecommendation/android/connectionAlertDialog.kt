package com.medicinerecommendation.android

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun connectionAlertDialog(activity: AppCompatActivity) = AlertDialog.Builder(activity).apply {
    setTitle("网络异常")
    setMessage("无法连接到网络，点击确定按钮后将返回到导航页面")
    setCancelable(false)
    setPositiveButton("确定") { _, _ ->
        SelfDiagnosisActivityCollector.returnToNavigationPage()
    }
}