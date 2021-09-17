package ru.salazarev.roadsaround

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.salazarev.roadsaround.di.app.AppComponent
import ru.salazarev.roadsaround.di.app.DaggerAppComponent

/** Основной класс приложения. */
class App : Application() {

    companion object {
        /** Объект основного компонента предоставления зависимостей.  */
        lateinit var appComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().app(this).build()
        setFireStore()
    }

    private fun setFireStore() {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        Firebase.firestore.firestoreSettings = settings
    }
}

/** Вывод текста на экран посредством [Toast].
 * @param message - текст выводимого сообзения.
 */
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()