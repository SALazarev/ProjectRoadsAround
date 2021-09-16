package ru.salazarev.roadsaround.network.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class GoogleMap(
    val map: GoogleMap,
    private val key: String,
    private val typeWork: TypeWork,
    private val failCallback: FailCallback
) {

    companion object {
        private const val DEFAULT_ZOOM = 15
        enum class TypeWork{
            VIEW,
            EDIT
        }
    }

    private var currentPolyline: Polyline? = null
    private var listMarker: MutableList<Marker> = mutableListOf()

    enum class DirectionType(val type: String) {
        Walking("walking"),
    }

    init {
        map.setOnMarkerClickListener { marker ->
            moveMarker(marker)
            true
        }
        map.setOnMapClickListener {
            when (typeWork){
                TypeWork.EDIT ->pickPoint(it, key)
                TypeWork.VIEW ->updateTitle()
            }

        }
    }

    private fun getUrl(origin: LatLng, dest: LatLng, key: String): String {
        // Origin of route
        val strOrigin = "origin=${origin.latitude},${origin.longitude}"
        // Destination of route
        val strDest = "destination=${dest.latitude},${dest.longitude}"
        // Mode
        val mode = "mode=${DirectionType.Walking.type}"
        // Building the parameters to the web service
        val parameters = "$strOrigin&$strDest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=${
            key
        }"
    }

    fun getRouteJsonUrl(): String? = if (listMarker.size == 2) getUrl(
        listMarker[0].position,
        listMarker[1].position,
        key
    ) else null


    private fun pickPoint(coordinate: LatLng, key: String) {
        if (listMarker.size < 2) {
            addMarker(coordinate)
        }
        if (listMarker.size == 2) {
            val url = getUrl(listMarker[0].position, listMarker[1].position, key)

            val user = Single.fromCallable {
                return@fromCallable UrlWorker().getRoad(url)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

            user.subscribe(
                {
                    if (currentPolyline != null) currentPolyline?.remove()
                    currentPolyline = map.addPolyline(it)
                    failCallback.onComplete(true)
                })
            { failCallback.onComplete(false) }

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

    fun setRouteByUrl(url: String) {

        Single.fromCallable {
            return@fromCallable UrlWorker().getRoad(url)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    addMarker(it.points[0])
                    addMarker(it.points[it.points.size - 1])
                    setCurrentLocation(it.points[0].latitude, it.points[0].longitude)
                    updateTitle()
                    currentPolyline = map.addPolyline(it)
                    failCallback.onComplete(true)
                })
            { failCallback.onComplete(false) }
    }

    private fun addMarker(coordinate: LatLng) {
        val markerOptions = MarkerOptions().position(coordinate)
        val marker = map.addMarker(markerOptions)
        listMarker.add(marker)
    }

    interface FailCallback {
        fun onComplete(status: Boolean)
    }
}