package com.iprism.adbots.utils

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

class MyApplication : Application(), DefaultLifecycleObserver {

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d("MyApplication", "App is in the foreground")
       // enqueueStatusWork("online")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d("MyApplication", "App is in the background")
       // enqueueStatusWork("offline")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d("MyApplication", "App is in the background")
        super.onDestroy(owner)
    }
}