package com.example.aop_part3_chapter3

import android.app.AlarmManager
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
        val model = saveAlarmModel(array[0].toInt(), array[1].toInt(), onoffCheck)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1000,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        if ((pendingIntent == null) and model.onoffCheck) {
            // 알람은 꺼져있는데, 데이터는 켜저있는 경우
            model.onoffCheck = false

        } else if ((pendingIntent != null) and model.onoffCheck.not()) {
            // 알람은 켜져있는데, 데이터는 꺼져있는 경우
            // 알람을 취소함
            pendingIntent.cancel()
        }

        renderView(model)

    }


    private fun initOnOffButton() {

        var onoffButton = findViewById<Button>(R.id.onOffButton)
        onoffButton.setOnClickListener {
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour, model.minute, model.onoffCheck.not())
            renderView(newModel)

            if (newModel.onoffCheck) {

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pendingIntent = PendingIntent.getBroadcast(
                    this, 1000,
                    Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent

                )


            } else {
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    1000,
                    Intent(this, AlarmReceiver::class.java),
                    PendingIntent.FLAG_NO_CREATE
                )
                pendingIntent?.cancel()
            }


            val pendingIntent = PendingIntent.getBroadcast(
                this,
                1000,
                Intent(this, AlarmReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE
            )


        }
    }

    private fun initChangeAlarmTimeButton() {

        var changeTimeButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeTimeButton.setOnClickListener {

            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { picker, hour, minute ->
                val model: AlarmDisplayModel = saveAlarmModel(hour, minute, false)

                renderView(model)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    1000,
                    Intent(this, AlarmReceiver::class.java),
                    PendingIntent.FLAG_NO_CREATE
                )
                pendingIntent?.cancel()


            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onoffCheck: Boolean): AlarmDisplayModel {

        val model = AlarmDisplayModel(hour, minute, onoffCheck)


        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString("alarm", model.makeDataForDB())
            putBoolean("onOff", model.onoffCheck)
            commit()
        }


        return model
    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply {
            text = model.ampmText
        }

        findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }

        findViewById<Button>(R.id.onOffButton).apply {
            text = model.onOffText
            tag = model
        }

    }


}