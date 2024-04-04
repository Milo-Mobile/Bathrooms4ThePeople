package com.milomobile.bathrooms4tp.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Throwable.report(tag: Pair<String, String>? = null) {
    val crashlytics = FirebaseCrashlytics.getInstance()

    tag?.let {
        crashlytics.setCustomKey(tag.first, tag.second)
    }
    crashlytics.recordException(this)
}