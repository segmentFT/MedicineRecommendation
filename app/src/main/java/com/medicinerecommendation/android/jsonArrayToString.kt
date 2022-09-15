package com.medicinerecommendation.android

import org.json.JSONArray

fun jsonArrayToString(jsonArray: JSONArray): String{
    val contentString = StringBuilder()

    for(i in 0 until jsonArray.length()) {
        contentString.append(jsonArray.getString(i))

        if(i < jsonArray.length() - 1)
            contentString.append(", ")
    }

    return contentString.toString()
}

fun stringBuilderToString(content: StringBuilder): String {
    val stringBuilder = StringBuilder()

    for(i in 0 until content.length) {
        stringBuilder.append(content[i])

        if(i < content.length - 1)
            stringBuilder.append(", ")
    }

    return stringBuilder.toString()
}
