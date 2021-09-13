package ru.salazarev.roadsaround.network

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.salazarev.googlemapapiexample.FetchUrl
import com.salazarev.googlemapapiexample.TaskLoadedCallback

class GoogleMap(val map: GoogleMap) {

    companion object {
        private const val DEFAULT_ZOOM = 15
    }

    private var currentPolyline: Polyline? = null
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

    private fun pickPoint(coordinate: LatLng, key: String) {
        if (listMarker.size == 0) {
            val markerOptions = MarkerOptions().position(coordinate)
            val marker = map.addMarker(markerOptions)
            listMarker += marker
        } else if (listMarker.size == 1) {
            val markerOpt = MarkerOptions().position(coordinate)
            val marker = map.addMarker(markerOpt)
            listMarker += marker
            val url = getUrl(
                listMarker[0].position,
                listMarker[1].position,
                key
            )
            FetchUrl(object : TaskLoadedCallback {
                override fun onTaskDone(vararg values: Any?) {
                    if (currentPolyline != null) currentPolyline?.remove()
                    currentPolyline = map.addPolyline(values[0] as PolylineOptions?)
                }

            }).execute(url)
        }
        if (listMarker.isNotEmpty()) updateTitle()
    }

    private fun moveMarker(marker: Marker) {
        marker.remove()
        listMarker.remove(marker)
        if (listMarker.size == 1) currentPolyline?.remove()
        updateTitle()
    }

    private fun updateTitle() {
        if (listMarker.isNotEmpty()) {
            listMarker[0].title = "Старт"
            listMarker[0].showInfoWindow()
        }
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM.toFloat())
        )
    }

    init {
        map.setOnMarkerClickListener { marker ->
            moveMarker(marker)
            true
        }
        map.setOnMapClickListener {
            pickPoint(it, "AIzaSyDuQuolV9HCZrMrYNj53CXwE29sVR2W3IQ")
        }
    }
}