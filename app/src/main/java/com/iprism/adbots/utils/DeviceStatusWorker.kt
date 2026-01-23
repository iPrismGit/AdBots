package com.iprism.adbots.utils

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.network.AdBotsApi

class DeviceStatusWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("DeviceStatusWorker", "Worker Started")
        val user = User(applicationContext)
        val userId = user.getUserDetails()[User.ID]

        return if (userId != null) {
            try {
                val request = UpdateDeviceStatusRequest(
                    deviceStatus = "offline",
                    userId = userId
                )
                val response = AdBotsApi.adBotsService.updateDeviceStatus(request)
                if (response.status) {
                    Log.d("DeviceStatusWorker", "Device status updated successfully")
                    Result.success()
                } else {
                    Result.retry()
                }
            } catch (e: Exception) {
                Result.retry()
            }
        } else {
            Result.failure()
        }
    }
}
