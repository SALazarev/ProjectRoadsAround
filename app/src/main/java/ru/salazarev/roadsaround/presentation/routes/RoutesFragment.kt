package ru.salazarev.roadsaround.presentation.routes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentProfileBinding
import ru.salazarev.roadsaround.databinding.FragmentRoutesBinding
import ru.salazarev.roadsaround.presentation.MainActivity

class RoutesFragment : Fragment() {

    private var _binding: FragmentRoutesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_routes_menu)
            title = context.getString(R.string.routes)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}