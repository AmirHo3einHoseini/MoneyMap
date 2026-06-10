package com.example.moneymap.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoneyMapApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}