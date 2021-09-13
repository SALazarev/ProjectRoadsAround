package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import ru.salazarev.roadsaround.network.GoogleMap
import javax.inject.Inject

class EditRoadViewModel() : ViewModel() {
    lateinit var map: GoogleMap
    private set
    fun setMap(googleMap: com.google.android.gms.maps.GoogleMap){
        map = GoogleMap(googleMap)
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        map.setCurrentLocation(latitude,longitude)
    }
}