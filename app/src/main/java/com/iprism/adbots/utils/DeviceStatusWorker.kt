package com.iprism.adbots.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.network.AdBotsApi
import java.net.UnknownHostException

class DeviceStatusWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("DeviceStatusWorker", "Worker Started")

        // 1. Get User ID safely
        val user = User(applicationContext)
        val userId = user.getUserDetails()[User.ID]

        if (userId == null) {
            Log.e("DeviceStatusWorker", "Failure: User ID is null")
            return Result.failure()
        }

        return try {
            val request = UpdateDeviceStatusRequest(
                deviceStatus = "offline",
                userId = userId
            )

            Log.d("DeviceStatusWorker", "Sending Request: $request")

            // 2. Execute API Call
            val response = AdBotsApi.adBotsService.updateDeviceStatus(request)

            if (response.status) {
                Log.d("DeviceStatusWorker", "Success: Status updated")
                Result.success()
            } else {
                Log.e("DeviceStatusWorker", "Server Error: ${response.message}")
                Result.failure()
            }
        } catch (e: UnknownHostException) {
            // 3. This catches the "Unable to resolve host" error
            Log.e("DeviceStatusWorker", "Network Error: Cannot find uzzto.com. Retrying...")
            Result.retry()
        } catch (e: Exception) {
            Log.e("DeviceStatusWorker", "Unexpected Error: ${e.localizedMessage}")
            Result.retry()
        }
    }
}