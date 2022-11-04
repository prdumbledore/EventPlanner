package com.eriksargsyan.eventplanner.data.database

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.eriksargsyan.eventplanner.util.Constants
import javax.inject.Inject

class WeatherSettings @Inject constructor(
    appContext: Context
) {
    private val sharedPref: SharedPreferences =
        appContext.getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)

    fun isExist(): Boolean = sharedPref.getBoolean(Constants.SHARED_PREF_TOKEN, false)

    fun setExistFlag(flag: Boolean) {
        sharedPref.edit {
            putBoolean(Constants.SHARED_PREF_TOKEN, flag)
        }
    }


}