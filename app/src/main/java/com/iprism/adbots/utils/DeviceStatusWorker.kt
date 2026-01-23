package com.iprism.adbots.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class DeviceStatusWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("DeviceStatusWorker", "🔥 Worker STARTED")

        delay(5000)

        Log.d("DeviceStatusWorker", "✅ Worker FINISHED")
        return Result.success()
    }
}
