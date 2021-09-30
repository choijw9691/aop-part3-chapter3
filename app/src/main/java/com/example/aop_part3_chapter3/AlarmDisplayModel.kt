package com.example.aop_part3_chapter3

data class AlarmDisplayModel(val hour: Int, val minute: Int, var onoffCheck: Boolean) {

    val timeText: String
        get() {
            val h = "0.2d%".format(if (hour < 12) hour else 12 - hour)
            val m = "0.2d%".format(minute)
            return "$h:$m"
        }

    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }


}
