package com.example.aop_part3_chapter3

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {


    private val timeTextView by lazy { findViewById<TextView>(R.id.timeTextView) }
    private val ampmTextView by lazy { findViewById<TextView>(R.id.ampmTextView) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 뷰초기화 2. 데이터 가져오기 3. 뷰에 데이터 그려주기
        initView()
        initOnOffButton()
        initChangeAlarmTimeButton()


    }

    private fun initView() {

        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)

        val timeTextView = sharedPreferences.getString("alarm", "09:00") ?: "09:00"
        val onoffCheck = sharedPreferences.getBoolean("onOff", false)
        var array = timeTextView.split(":")
        saveAlarmModel(array[0].toInt(), array[1].toInt(), onoffCheck)

    }


    private fun initOnOffButton() {

        var onoffButton = findViewById<Button>(R.id.onOffButton)
        onoffButton.setOnClickListener {


        }
    }

    private fun initChangeAlarmTimeButton() {

        var changeTimeButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeTimeButton.setOnClickListener {

            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { picker, hour, minute ->
                val model:AlarmDisplayModel = saveAlarmModel(hour, minute, true)
                timeTextView.text = model.timeText
                ampmTextView.text = model.ampmText

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    1000,
                    Intent(this, AlarmReceiver::class.java),
                    PendingIntent.FLAG_NO_CREATE
                )

                pendingIntent.cancel()


            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onoffCheck: Boolean): AlarmDisplayModel {

        val model = AlarmDisplayModel(hour, minute, true)


        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString("alarm", model.makeDataForDB())
            putBoolean("onOff", model.onoffCheck)
            commit()
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1000,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        if ((pendingIntent == null) and model.onoffCheck) {
            model.onoffCheck = false
        } else if ((pendingIntent != null) and model.onoffCheck.not()) {
            pendingIntent.cancel()
        }
        return model
    }

}