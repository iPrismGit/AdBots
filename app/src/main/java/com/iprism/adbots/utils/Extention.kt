package com.iprism.adbots.utils

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.iprism.adbots.utils.User

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ProgressBar.showProgress() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hideProgress() {
    this.visibility = View.GONE
}

fun Context.getUserDetails(): HashMap<String, String?> {
    val user = User(this)
    return user.getUserDetails()
}

fun Button.setEnabledState(enabled: Boolean) {
    isEnabled = enabled
    isClickable = enabled
    isActivated = enabled
}
