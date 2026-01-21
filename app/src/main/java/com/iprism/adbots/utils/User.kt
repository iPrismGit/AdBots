package com.iprism.adbots.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class User(var context: Context) {

    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0

    init {
        sharedPreferences = context.applicationContext.getSharedPreferences("AdBots", PRIVATE_MODE)
        editor = sharedPreferences.edit()
    }

    fun storeUserDetails(id: String?, authToken: String?, mobile : String?) {
        editor.putString(ID, id)
        editor.putString(AUTH_TOKEN, authToken)
        editor.putString(MOBILE, mobile)
        editor.commit()
    }

    fun loginUser() {
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.commit()
    }

    fun saveAddress() {
        editor.putBoolean(IS_ADDRESS, true)
        editor.commit()
    }

    fun getUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[ID] = sharedPreferences.getString(ID, null)
        user[AUTH_TOKEN] = sharedPreferences.getString(AUTH_TOKEN, null)
        user[MOBILE] = sharedPreferences.getString(MOBILE, null)
        user[LANG] =  sharedPreferences.getString(LANG, "en")!!
        return user
    }

    fun storeLang(lang : String)  {
        editor.putString(LANG, lang)
        editor.commit()
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false)
    }

    fun isAddress(): Boolean {
        return sharedPreferences.getBoolean(IS_ADDRESS, false)
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }

    companion object {
        const val ID = "id"
        const val AUTH_TOKEN = "token"
        const val MOBILE = "mobile"
        const val IS_USER_LOGIN = "isUserLogin"
        const val IS_ADDRESS = "isAddress"
        const val LANG = "en"
    }
}