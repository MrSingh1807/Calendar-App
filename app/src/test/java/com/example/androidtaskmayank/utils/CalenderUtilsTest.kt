package com.example.androidtaskmayank.utils

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek

class CalenderUtilsTest {

    private lateinit var calender: CalenderUtils

    @Before
    fun setUp() {
        calender = CalenderUtils()
        println("execute before all text cases")
    }

    @Test
    fun test_ExtractDateTimeFromTimestamp() {
        //todo: date : 01-01-2024 - 12:00:00  => 1704090600000

        val dateMillis = 1704090600000
        val dateTime = calender.extractDateTimeFromTimestamp(dateMillis)

        assertEquals("Current date (01/01/2024) : Year => ", 2024, dateTime.year)
        assertEquals("Current date (01/01/2024) : Month Value => ", 1, dateTime.monthValue)
        assertEquals("Current date (01/01/2024) : Day Value => ", 1, dateTime.dayOfMonth)

        assertNotEquals("Current date (01/01/2024) : Year not equal 20001 => ", 2001, dateTime.year)
    }

    @Test
    fun test_DateToMillis() {

        val dateMillis = 1704090600000
        val dateTime = calender.dateToMillis(2024, 1, 1, 12, 0, 0)

        assertEquals("Current Date : (01/01/2024) =>  ", dateMillis, dateTime)
    }

    @Test
    fun test_CompareTwoDatesIsSame() {

        val dateAt12PM = calender.dateToMillis(2024, 1, 1, 12, 0, 0)
        val dateAt5PM = calender.dateToMillis(2024, 1, 1, 17, 0, 0)

        val differentDate = calender.dateToMillis(2024, 1, 26, 12, 0, 0)

        assertEquals(
            "Same date with different time -> [ First Date(01/01/2024 12:00:00) : Second Date(01/01/2024 17:00:00)] => ",
            true, calender.compareTwoDatesIsSame(dateAt12PM, dateAt5PM)
        )
        assertEquals(
            "Compare Two Diff Dates [ First Date(01/01/2024 12:00:00) : Second Date(26/01/2024 12:00:00)  => )",
            false, calender.compareTwoDatesIsSame(dateAt12PM, differentDate)
        )
        assertNotEquals(
            "Compare Two Dates [ First Date(01/01/2024 17:00:00) : Second Date(26/01/2024 12:00:00)  => )",
            true, calender.compareTwoDatesIsSame(dateAt5PM, differentDate)
        )
    }

    @Test
    fun test_TotalDaysInMonth() {
        //todo : July => 31 days

        val daysInJuly = calender.getTotalDaysInMonth(2024, 7)
        val daysInFebruary = calender.getTotalDaysInMonth(2024, 2)

        assertEquals("July 2024 (31 days) => ", 31, daysInJuly)
        assertNotEquals("February 2024 (28 days) : Not Equal coz leap year => ", 28, daysInFebruary)
        assertEquals("February 2024 (29 days : Leap year) => ", 29, daysInFebruary) // leap year 2024

    }

    @Test
    fun test_FirstDayOfMonth() {

        val firstDayInJuly = calender.getFirstDayOfMonth(2024, 7)
        val firstDayInFebruary = calender.getFirstDayOfMonth(2024, 2)

        assertEquals("July 2024, fist day - Monday ", DayOfWeek.MONDAY, firstDayInJuly)
        assertEquals("February 2024, fist day - Thursday ", DayOfWeek.THURSDAY, firstDayInFebruary)
    }

    @Test
    fun formatTimeMillisToString() {
        //todo: date : 01-01-2024 - 12:00:00  => 1704090600000
        val date = 1704090600000

        assertEquals(
            "Current date (01/01/2024) => millis - 1704090600000",
            "2024-01-01 12:00:00",
            CalenderUtils.formatTimeMillisToString(date, "yyyy-MM-dd HH:mm:ss")
        )
        assertEquals(
            "Current date (01/01/2024) => millis - 1704090600000",
            "2024-01-01 12:00",
            CalenderUtils.formatTimeMillisToString(date, "yyyy-MM-dd HH:mm")
        )
        assertEquals(
            "Current date (01/01/2024) => millis - 1704090600000",
            "2024-01-01", CalenderUtils.formatTimeMillisToString(date, "yyyy-MM-dd")
        )
        assertEquals(
            "Current date (01/01/2024) => millis - 1704090600000",
            "2024/01/01 12:00:00",
            CalenderUtils.formatTimeMillisToString(date, "yyyy/MM/dd HH:mm:ss")
        )
        assertNotEquals(
            "Current date (01/01/2024) => millis - 1704090600000",
            "2024-01-01 12:00:00",
            CalenderUtils.formatTimeMillisToString(date, "yyyy/MM/dd HH:mm:ss")
        )
    }

}
