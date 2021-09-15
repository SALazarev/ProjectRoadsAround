package ru.salazarev.roadsaround.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentMainBinding
import javax.inject.Inject
import javax.inject.Named

class MainFragment : Fragment() {

    companion object{
        const val EVENT_KEY = "EVENT_KEY"
    }

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
        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build().inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureNavigationBottom()
    }

    private fun configureNavigationBottom() {
        binding.bottomNav.bottomNav.setupWithNavController(navSecondController)
        binding.bottomNav.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileFragment -> {
                    navSecondController.navigate(R.id.action_global_profileFragment)
                    true
                }
                R.id.routesFragment -> {
                    navSecondController.navigate(R.id.action_global_routesFragment)
                    true
                }
                R.id.searchEventFragment -> {
                    navSecondController.navigate(R.id.action_global_searchEventFragment)
                    true
                }
                else -> false
            }
        }
        binding.bottomNav.bottomNav.setOnItemReselectedListener { }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}