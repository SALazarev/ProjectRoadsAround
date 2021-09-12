package ru.salazarev.roadsaround.presentation.editroad

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.salazarev.googlemapapiexample.FetchUrl
import com.salazarev.googlemapapiexample.TaskLoadedCallback
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentEditRoadBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import javax.inject.Inject


class EditRoadFragment : Fragment(), OnMapReadyCallback {

    enum class DirectionType(val type: String) {
        Driving("driving"),
        Walking("walking"),
        Cycling("cycling")
    }

    companion object {
        private const val REQUEST_CODE = 100
        private const val DEFAULT_ZOOM = 15
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var lastKnownLocation: Location? = null
    private lateinit var mMap: GoogleMap
    private var currentPolyline: Polyline? = null
    private val listMarker: MutableList<Marker> = mutableListOf()

    private var _binding: FragmentEditRoadBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditRoadViewModel

    @Inject
    lateinit var editRoadViewModelFactory: EditRoadViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditRoadBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)
        viewModel =
            ViewModelProvider(this, editRoadViewModelFactory).get(EditRoadViewModel::class.java)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_edit_road_menu)
            title = context.getString(R.string.settings_route)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener {
                (activity as MainActivity).navController
                    .navigate(R.id.action_editRoadFragment_to_editEventFragment)
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_complete_edit_road -> {
                        val bundle = Bundle().apply {
                            putString("ROAD", "POINTS")
                        }
                        (activity as MainActivity).navController
                            .navigate(R.id.action_editRoadFragment_to_editEventFragment, bundle)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mapLocation()

        mMap.setOnMarkerClickListener { marker ->
            marker.remove()
            listMarker.remove(marker)
            if (listMarker.size == 1) currentPolyline?.remove()
            updateTitle()
            true
        }
        mMap.setOnMapClickListener {
            if (listMarker.size == 0) {
                val markerOpt = MarkerOptions().position(it)
                val marker = mMap.addMarker(markerOpt)
                listMarker += marker
            } else if (listMarker.size == 1) {
                val markerOpt = MarkerOptions().position(it)
                val marker = mMap.addMarker(markerOpt)
                listMarker += marker
                val url = getUrl(
                    listMarker[0].position,
                    listMarker[1].position,
                    DirectionType.Walking.type
                )
                FetchUrl(object: TaskLoadedCallback{
                    override fun onTaskDone(vararg values: Any?) {
                        if (currentPolyline != null) currentPolyline?.remove()
                        currentPolyline = mMap.addPolyline(values[0] as PolylineOptions?)
                    }

                }).execute(url, DirectionType.Walking.type)
            }
            if (listMarker.isNotEmpty())  updateTitle()
        }
    }

    private fun updateTitle() {
        if (listMarker.isNotEmpty()){
            listMarker[0].title="Старт"
            listMarker[0].showInfoWindow()
        }
    }

    private fun mapLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getDeviceLocation()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_CODE
                )
            }
        } else {
            getDeviceLocation()
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )
                    }
                } else {
                    mMap.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                    )
                    mMap.uiSettings?.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: SecurityException) {

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation()
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String? {
        // Origin of route
        val strOrigin = "origin=${origin.latitude},${origin.longitude}"
        // Destination of route
        val strDest = "destination=${dest.latitude},${dest.longitude}"
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$strOrigin&$strDest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=${
            getString(
                R.string.google_maps_key
            )
        }"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}