package ru.salazarev.roadsaround.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/** Базовый класс для всех объектов ViewModel приложения */
open class BaseViewModel : ViewModel() {

    /** Контейнер объектов [Disposable]. */
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}