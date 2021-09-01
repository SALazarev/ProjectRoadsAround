package ru.salazarev.roadsaround.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentMainBinding
import ru.salazarev.roadsaround.presentation.MainActivity

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val alertDialog: AlertDialog by lazy { createAlertDialog() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNav.setupWithNavController((activity as MainActivity).navController)
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_main_menu)
            title = context.getString(R.string.main)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_logout_24)
            setNavigationOnClickListener { alertDialog.show() }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = (activity as MainActivity).navController
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

    }

    private fun createAlertDialog(): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.you_shure_log_out))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, which -> logout() }
            .setNegativeButton(getString(R.string.no)) { dialog, id -> dialog.cancel() }
        return builder.create()
    }

    private fun logout() {
        (activity as MainActivity).fireBaseAuth.signOut()
        (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_authFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}