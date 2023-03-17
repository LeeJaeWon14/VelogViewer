package com.jeepchief.velogviewer.util

import android.content.Context
import android.content.SharedPreferences

class Pref(private val context : Context) {
    private val PREF_NAME = "PreferenceForVV"
    private var preference : SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val FIRST_RUN = "FIRST_RUN"
        const val USER_ID = "USER_ID"

        private var instance : Pref? =null
        @Synchronized
        fun getInstance(context: Context) : Pref? {
            if(instance == null)
                instance = Pref(context)

            return instance
        }
    }

    fun getString(id: String) : String {
        return preference.getString(id, "") ?: ""
    }

    fun getBoolean(id: String) : Boolean {
        return preference.getBoolean(id, false)
    }

    fun setValue(id: String?, value: String) : Boolean {
        return preference.edit()
            .putString(id, value)
            .commit()
    }

    fun removeValue(id: String?) : Boolean {
        return preference.edit()
            .remove(id)
            .commit()
    }
}