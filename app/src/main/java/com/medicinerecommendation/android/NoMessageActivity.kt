package com.medicinerecommendation.android

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NoMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_message_layout)

        finishActivities()
    }

    companion object {
        private val activities = ArrayList<Activity>()

        fun addActivities(activity: Activity) {
            activities.add(activity)
        }

        fun finishActivities() {
            for(activity in activities) {
                if(!activity.isFinishing)
                    activity.finish()
            }
        }
    }
}