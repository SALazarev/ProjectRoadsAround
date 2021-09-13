package ru.salazarev.roadsaround.presentation.editroad

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentEditRoadBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast
import javax.inject.Inject


class EditRoadFragment : Fragment(), OnMapReadyCallback {

    @Inject
    lateinit var editRoadViewModelFactory: EditRoadViewModelFactory

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var _binding: FragmentEditRoadBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditRoadViewModel

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) getDeviceLocation()
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditRoadBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)
        viewModel =
            ViewModelProvider(this, editRoadViewModelFactory).get(EditRoadViewModel::class.java)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

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
        viewModel.setMap(googleMap)
        checkLocatePermission()
    }

    private fun checkLocatePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getDeviceLocation()
        } else {
            if (
                (activity as MainActivity).hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                getDeviceLocation()
            } else {
                requestPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun getDeviceLocation() {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (task.result != null) {
                        viewModel.setCurrentLocation(task.result.latitude, task.result.longitude)
                    }
                }
            }
        } catch (e: SecurityException) {
            requireActivity().toast(getString(R.string.could_not_get_the_location_data))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}