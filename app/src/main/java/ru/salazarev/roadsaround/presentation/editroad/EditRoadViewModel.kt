package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import ru.salazarev.roadsaround.network.GoogleMap
import javax.inject.Inject

class EditRoadViewModel @Inject constructor(val mapApi: GoogleMap) : ViewModel() {
    fun getUrl(position: LatLng, position1: LatLng, key: String) =
        mapApi.getUrl(position, position1, key)
}