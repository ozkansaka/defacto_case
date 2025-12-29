package com.defacto.movieapp.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {
    fun getYearFromDate(date: String?): String? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsedDate = sdf.parse(date ?: return null)
            Calendar.getInstance().apply {
                time = parsedDate!!
            }.get(Calendar.YEAR).toString()
        } catch (e: Exception) {
            null
        }
    }
}