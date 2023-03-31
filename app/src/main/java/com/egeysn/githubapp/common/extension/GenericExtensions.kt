package com.egeysn.githubapp.common.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)
