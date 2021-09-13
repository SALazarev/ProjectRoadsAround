package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.network.map.GoogleMap

class EditRoadViewModel() : ViewModel() {
    lateinit var map: GoogleMap
    private set

    val resultCreateRoute = MutableLiveData<Boolean>()
    val markers = MutableLiveData<String?>()

    fun setMap(googleMap: com.google.android.gms.maps.GoogleMap, key: String){
        map = GoogleMap(googleMap, key, object : GoogleMap.FailCallback{
            override fun onComplete(status: Boolean) {
                resultCreateRoute.value = status
            }
        })
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        map.setCurrentLocation(latitude,longitude)
    }

    fun getMarkers(){
       markers.value = map.getMarkers()
    }
}