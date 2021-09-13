package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.network.map.GoogleMap

class EditRoadViewModel() : ViewModel() {
    lateinit var map: GoogleMap
    private set

    val result = MutableLiveData<Boolean>()

    fun setMap(googleMap: com.google.android.gms.maps.GoogleMap){
        map = GoogleMap(googleMap, object : GoogleMap.FailCallback{
            override fun onComplete(status: Boolean) {
                result.value = status
            }

        })
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        map.setCurrentLocation(latitude,longitude)
    }
}