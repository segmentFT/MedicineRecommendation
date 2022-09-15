package com.medicinerecommendation.android

import android.app.Activity

object SelfDiagnosisActivityCollector {
    val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun returnToNavigationPage() {
        for(activity in activities) {
            if(!activity.isFinishing)
                activity.finish()
        }
    }
}