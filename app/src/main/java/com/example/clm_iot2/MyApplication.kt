package com.example.clm_iot2

import android.app.Application
import com.example.clm_iot2.security.TokenManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        android.util.Log.d("MyApplication", "MyApplication onCreate called")
    }
}
