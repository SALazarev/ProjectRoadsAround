package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.network.map.GoogleMap

/** ViewModel для фрагмента [EditRoadViewModel]. */
class EditRoadViewModel : ViewModel() {

    /** Объект работы с картой GooglePlayApi. */
    lateinit var map: GoogleMap
        private set

    /** Результат создания события. */
    val resultCreateRoute = MutableLiveData<Boolean>()

    /** Конфигурация карты
     * @param googleMap - объект работы с картой GooglePlayApi.
     * @param key - ключ пользователя GooglePlayApi.
     * @param typeWork - тип работы с картой.
     */
    fun configureMap(
        googleMap: com.google.android.gms.maps.GoogleMap,
        key: String,
        typeWork: GoogleMap.Companion.TypeWork
    ) {
        map = GoogleMap(googleMap, key, typeWork, object : GoogleMap.CompleteCallback {
            override fun onComplete(status: Boolean) {
                resultCreateRoute.value = status
            }
        })
    }

    /**
     * Установка камеры в текущем местоположении.
     * @param latitude - широта.
     * @param longitude - долгота.
     */
    fun setCurrentLocation(latitude: Double, longitude: Double) {
        map.setCurrentLocation(latitude, longitude)
    }

    /** Предоставление ссылки на маршрут.
     * @return ссылка на маршрут.
     */
    fun getRoute(): String? = map.getRouteJsonUrl()

    /** Установка маршрута через ссылку.
     * @param oute - ссылка на маршрут.
     */
    fun setRoute(route: String) {
        map.setRouteByUrl(route)
    }
}