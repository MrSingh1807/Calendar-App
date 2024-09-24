package com.example.androidtaskmayank.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


class CalenderUtils {

    fun getTodayDate(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return Triple(year, month, day)
    }

    fun extractDateTimeFromTimestamp(timestamp: Long): LocalDateTime {
        val instant = Instant.ofEpochMilli(timestamp)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    fun dateToMillis(
        year: Int, month: Int, day: Int,
        hour: Int = 12, minute: Int = 0, second: Int = 0
    ): Long {
        val localDate = LocalDate.of(year, month, day)
        val localTime = LocalTime.of(hour, minute, second)
        val localDateTime = localDate.atTime(localTime)
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return instant.toEpochMilli()
    }


    fun compareTwoDatesIsSame(date1: Long, date2: Long): Boolean {
        val firstDate = extractDateTimeFromTimestamp(date1)
        val secondDate = extractDateTimeFromTimestamp(date2)

        return firstDate.year == secondDate.year && firstDate.monthValue == secondDate.monthValue
                && firstDate.dayOfMonth == secondDate.dayOfMonth
    }

    fun getTotalDaysInMonth(year: Int, month: Int): Int {
        val yearMonth = YearMonth.of(year, month)
        return yearMonth.lengthOfMonth()
    }

    fun getFirstDayOfMonth(year: Int, month: Int): DayOfWeek? {
        val yearMonth = YearMonth.of(year, month)
        return LocalDate.of(yearMonth.year, yearMonth.month, 1).dayOfWeek
    }

    companion object {
        fun formatTimeMillisToString(
            timeInMillis: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"
        ): String {
            val dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern(pattern)
            return dateTime.format(formatter)
        }

    }

}

