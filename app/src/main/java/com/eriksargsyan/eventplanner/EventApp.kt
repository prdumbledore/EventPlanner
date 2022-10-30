package com.eriksargsyan.eventplanner

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eriksargsyan.eventplanner.di.AppComponent
import com.eriksargsyan.eventplanner.di.DaggerAppComponent


class EventApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).build()

    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is EventApp -> appComponent
        else -> applicationContext.appComponent
    }

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}