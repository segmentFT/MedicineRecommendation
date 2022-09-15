package com.medicinerecommendation.android

import android.content.Context

fun convertDpToPx(context: Context, dp: Int): Int {
    val scale = context.resources.displayMetrics.density

    return (dp.toFloat() * scale + 0.5).toInt()
}