package com.egeysn.githubapp.common.extension

fun Long?.safeGet(default: Long = 0): Long {
    return this ?: default
}

fun Int?.safeGet(default: Int = 0): Int {
    return this ?: default
}

fun Double?.safeGet(default: Double = 0.0): Double {
    return this ?: default
}

fun String?.safeGet(): String {
    return this ?: ""
}
