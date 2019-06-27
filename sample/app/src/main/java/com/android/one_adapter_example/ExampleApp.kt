package com.android.one_adapter_example

import android.app.Application
import com.android.one_adapter_example.models.ModelGenerator
import com.android.one_adapter_example.persistence.RoomDB
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

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