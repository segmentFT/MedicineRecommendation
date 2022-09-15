package com.medicinerecommendation.android

fun occupationNameToId(occupationName: String): String {
    return when(occupationName) {
        "工人" -> "3"
        "农民" -> "4"
        "教师" -> "11"
        "服务员" -> "7"
        "司机" -> "5"
        "厨师" -> "6"
        "办公室职员" -> "10"
        "医生" -> "12"
        "护士" -> "12"
        "公务员" -> "9"
        "销售" -> "8"
        "采购" -> "8"
        "其他" -> "13"
        else -> ""
    }
}