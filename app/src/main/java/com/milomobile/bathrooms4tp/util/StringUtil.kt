package com.milomobile.bathrooms4tp.util

import java.util.Locale

fun String.capitalize() = this.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
}