package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import ru.salazarev.roadsaround.network.GoogleMap
import javax.inject.Inject

class EditRoadViewModel() : ViewModel() {
//    fun getUrl(position: LatLng, position1: LatLng, key: String) =
//        mapApi.getUrl(position, position1, key)
//
//    fun pickPoint(coordinate: LatLng, key: String){
//        mapApi.pickPoint(coordinate, key)
//    }
//
//    fun moveMarker(marker: Marker) {
//        mapApi.moveMarker(marker)
//    }

    lateinit var map: GoogleMap
    private set
    fun setMap(googleMap: com.google.android.gms.maps.GoogleMap){
        map = GoogleMap(googleMap)
    }
}