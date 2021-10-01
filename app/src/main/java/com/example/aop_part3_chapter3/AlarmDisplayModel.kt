package com.example.aop_part3_chapter3

import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
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

    fun makeDataForDB():String{
        return "$hour:$minute"
    }

}
