package com.idanatz.sample

import android.app.Application

class ExampleApp : Application() {

    companion object {
        private lateinit var Instance: ExampleApp

        fun getInstance() = Instance
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this
    }
}