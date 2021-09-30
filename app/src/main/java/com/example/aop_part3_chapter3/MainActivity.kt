package com.example.aop_part3_chapter3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            // 1. 뷰초기화 2. 데이터 가져오기 3. 뷰에 데이터 그려주기
     initView()
        initData()


    }

    private fun initData() {
        TODO("Not yet implemented")
    }

    private fun initView() {
        TODO("Not yet implemented")
    }
}