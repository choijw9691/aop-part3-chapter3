package com.example.aop_part3_chapter3

import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.util.Log
import android.widget.TimePicker
import kotlin.math.min

data class AlarmDisplayModel(val hour: Int, val minute: Int, var onoffCheck: Boolean) {

    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else  hour-12)
            val m = "%02d".format(minute)
            return "$h:$m"
        }

    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    val onOffText: String
        get() {
            Log.d("getofftext",onoffCheck.toString())
            return if (onoffCheck) "알람 끄기" else "알람 켜기"
        }

    fun makeDataForDB():String{
        return "$hour:$minute"
    }

}
