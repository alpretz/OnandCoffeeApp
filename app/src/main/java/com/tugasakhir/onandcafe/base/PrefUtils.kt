package com.tugasakhir.onandcafe.base

import android.content.Context
import android.content.SharedPreferences


class PrefUtils(context: Context, private var prefKey: String) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(this.prefKey, Context.MODE_PRIVATE)

    fun saveToPref(key: String?, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    fun saveToPref(key: String?, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun saveToPref(key: String?, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getFromPref(key: String?): String? {
        return preferences.getString(key, "")
    }

    fun getFromPrefInt(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    fun getFromPrefBoolean(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun removeFromPref(key: String) {
        preferences.edit().remove(key).apply()
    }

    companion object {
        const val AUTH_PREF = "auth_pref"
        const val AUTH_ID_PREF = "auth_id_pref"
        const val AUTH_ADMIN_PREF = "auth_admin_pref"
    }

}