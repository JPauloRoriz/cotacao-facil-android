package com.example.cotacaofacil.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

class SharedPreferencesHelper(
    private var sharedPreferences: SharedPreferences,
    private var sharedEncryptedPreferences: EncryptedSharedPreferences
) {

    val KEY_USER_LOGIN = "KEY_USER_LOGIN"

    fun setStringSecret(key: String?, value: String?, context: Context) {
        val editor = sharedEncryptedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringSecret(key: String?, context: Context, valueDefault: String?): String? {
        return sharedEncryptedPreferences.getString(key, valueDefault)
    }

    fun setString(key: String?, value: String?, context: Context) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?, context: Context, valueDefault: String?): String? {
        return sharedPreferences.getString(key, valueDefault)
    }
}