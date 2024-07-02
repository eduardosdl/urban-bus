package com.exemple.urbanbus.utils

fun String.limitTitleLength(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength) + "..."
    } else {
        this
    }
}