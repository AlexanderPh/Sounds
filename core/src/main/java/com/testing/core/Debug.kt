package com.testing.core

import android.util.Log

const val LOG_TAG = "DEBUG_LOG_TAG"

fun log(message : String) = Log.i(LOG_TAG, message)
fun log(obj : Any) = log(obj.toString())

