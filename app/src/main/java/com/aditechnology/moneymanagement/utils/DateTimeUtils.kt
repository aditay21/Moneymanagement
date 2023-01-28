package com.aditechnology.moneymanagement.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    companion object {
    const val DATE_FORMAT =  "EEE MMM dd yyyy"
    const val TIME_FORMAT =  "hh:mm a"


        fun getDate(): String {
            val cal = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(DATE_FORMAT)
            return dateFormat.format(cal.time)
        }

        fun getTime(): String {
            val cal = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(TIME_FORMAT)
            return dateFormat.format(cal.time)
        }
       fun getTimeStampFromDate(date :String) :Long{
            val formatter: DateFormat = SimpleDateFormat(DATE_FORMAT)
            val date = formatter.parse(date) as Date
            return date.time
       }

        fun getTimeStampFromTime(time: String): Long {
            val formatter: DateFormat = SimpleDateFormat(TIME_FORMAT)
            val date = formatter.parse(time) as Date
            return date.time
        }

        fun getDateFromTimeStamp(date: String): String {
            val formatter: DateFormat = SimpleDateFormat(DATE_FORMAT)
            return formatter.format(Date(date.toLong()))
        }

        fun getTimeFromTimeStamp(time: String): String {
            val formatter: DateFormat = SimpleDateFormat(TIME_FORMAT)
            return formatter.format(Date(time.toLong()))

        }
    }
}