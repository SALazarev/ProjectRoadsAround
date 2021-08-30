package ru.salazarev.roadsaround

import android.app.Application
import android.content.Context
import android.widget.Toast
import ru.salazarev.roadsaround.di.AppComponent
import ru.salazarev.roadsaround.di.DaggerAppComponent

class App : Application() {
    companion object {
        fun getComponent(context: Context) = (context.applicationContext as App).component
    }

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()