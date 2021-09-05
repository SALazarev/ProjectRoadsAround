package ru.salazarev.roadsaround

import android.app.Application
import android.content.Context
import android.widget.Toast
import ru.salazarev.roadsaround.di.AppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()