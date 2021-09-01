package ru.salazarev.roadsaround.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentMainBinding
import ru.salazarev.roadsaround.di.DaggerAppComponent
import ru.salazarev.roadsaround.presentation.MainActivity
import javax.inject.Inject
import javax.inject.Named

class MainFragment : Fragment() {

    @Inject
    @Named("secondNavController")
    lateinit var navSecondController: NavController

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        DaggerAppComponent.builder().fragmentManager(childFragmentManager).build().inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNav.bottomNav.setupWithNavController(navSecondController)
       // binding.bottomNav.bottomNav.setOnNavigationItemSelectedListener { it.onNavDestinationSelected(navSecondController) || super.onOptionsItemSelected(it) }
        binding.bottomNav.bottomNav.setOnNavigationItemReselectedListener {  }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}