package com.medicinerecommendation.android

fun convertGenderToUTF8(gender: String): String {
    return when(gender) {
        "性别_男" -> "%u6027%u522B_%u7537"
        "性别_女" -> "%u6027%u522B_%u5973"
        else -> ""
    }
}

fun convertAgeRangeToUTF8(ageRange: String): String {
    return when(ageRange) {
        "疾病年龄_新生儿（一个月内）" -> "%u75BE%u75C5%u5E74%u9F84_%u65B0%u751F%u513F%uFF08%u4E00%u4E2A%u6708%u5185%uFF09"
        "疾病年龄_婴儿（2~11个月）" -> "%u75BE%u75C5%u5E74%u9F84_%u5A74%u513F%uFF082~11%u4E2A%u6708%uFF09"
        "疾病年龄_儿童（1~12岁）" -> "%u75BE%u75C5%u5E74%u9F84_%u513F%u7AE5%uFF081~12%u5C81%uFF09"
        "疾病年龄_少年（13~18岁）" -> "%u75BE%u75C5%u5E74%u9F84_%u5C11%u5E74%uFF0813~18%u5C81%uFF09"
        "疾病年龄_青年（19~29岁）" -> "%u75BE%u75C5%u5E74%u9F84_%u9752%u5E74%uFF0819~29%u5C81%uFF09"
        "疾病年龄_壮年（30~39岁）" -> "%u75BE%u75C5%u5E74%u9F84_%u58EE%u5E74%uFF0830~39%u5C81%uFF09"
        "疾病年龄_中年（40~49岁）" -> "%u75BE%u75C5%u5E74%u9F84_%u4E2D%u5E74%uFF0840~49%u5C81%uFF09"
        "疾病年龄_中老年（50~64岁）" -> "%u75BE%u75C5%u5E74%u9F84_%u4E2D%u8001%u5E74%uFF0850~64%u5C81%uFF09"
        "疾病年龄_老年（65以上）" -> "%u75BE%u75C5%u5E74%u9F84_%u8001%u5E74%uFF0865%u4EE5%u4E0A%uFF09"
        else -> ""
    }
}

fun convertPositionToUTF8(position: String): String {
    return when(position) {
        "头部" -> "%u5934%u90E8"
        "胸部" -> "%u80F8%u90E8"
        "上肢" -> "%u4E0A%u80A2"
        "腹部" -> "%u8179%u90E8"
        "下肢" -> "%u4E0B%u80A2"
        "颈部" -> "%u9888%u90E8"
        "背部" -> "%u80CC%u90E8"
        "骨" -> "%u9AA8"
        "全身" -> "%u5168%u8EAB"
        "腰部" -> "%u8170%u90E8"
        "盆腔" -> "%u76C6%u8154"
        "臀部" -> "%u81C0%u90E8"
        "男性生殖" -> "%u7537%u6027%u751F%u6B96"
        "女性生殖" -> "%u5973%u6027%u751F%u6B96"
        else -> ""
    }
}