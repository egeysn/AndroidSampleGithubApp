package com.egeysn.githubapp.common.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun View.showSoftKeyboard() {
    post {
        if (this.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
