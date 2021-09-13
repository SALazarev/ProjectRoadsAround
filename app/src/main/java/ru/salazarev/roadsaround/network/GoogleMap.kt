package ru.salazarev.roadsaround.network

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import javax.inject.Inject

class GoogleMap @Inject constructor(): OnMapReadyCallback {

    companion object {
        private const val REQUEST_CODE = 100
        private const val DEFAULT_ZOOM = 15
    }

    private val listMarker: MutableList<Marker> = mutableListOf()

    enum class DirectionType(val type: String) {
        Driving("driving"),
        Walking("walking"),
        Cycling("cycling")
    }

     fun getUrl(origin: LatLng, dest: LatLng, key: String): String? {
        // Origin of route
        val strOrigin = "origin=${origin.latitude},${origin.longitude}"
        // Destination of route
        val strDest = "destination=${dest.latitude},${dest.longitude}"
        // Mode
        val mode = "mode=${DirectionType.Walking}"
        // Building the parameters to the web service
        val parameters = "$strOrigin&$strDest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=${
            key
        }"
    }

    override fun onMapReady(p0: GoogleMap?) {
        TODO("Not yet implemented")
    }
}