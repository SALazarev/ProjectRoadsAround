package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.network.map.GoogleMap

class EditRoadViewModel : ViewModel() {
    lateinit var map: GoogleMap
        private set

    val resultCreateRoute = MutableLiveData<Boolean>()

    fun setMap(
        googleMap: com.google.android.gms.maps.GoogleMap,
        key: String,
        typeWork: GoogleMap.Companion.TypeWork
    ) {
        map = GoogleMap(googleMap, key, typeWork, object : GoogleMap.FailCallback {
            override fun onComplete(status: Boolean) {
                resultCreateRoute.value = status
            }
        })
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        map.setCurrentLocation(latitude, longitude)
    }

    fun getRoute(): String? = map.getRouteJsonUrl()

    fun setRoute(route: String) {
        map.setRouteByUrl(route)
    }
}