package com.idanatz.oneadapter.helpers

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.idanatz.oneadapter.test.R

class TestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) // remove title bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) // remove notification bar
        setContentView(R.layout.activity_test)
    }
}