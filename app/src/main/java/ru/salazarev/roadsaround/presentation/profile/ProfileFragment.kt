package ru.salazarev.roadsaround.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentProfileBinding
import ru.salazarev.roadsaround.presentation.MainActivity

class ProfileFragment : Fragment() {


    private val alertDialog: AlertDialog by lazy { createAlertDialog() }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_profile_menu)
            title = context.getString(R.string.profile)
            navigationContentDescription = context.getString(R.string.profile)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_logout_24)
            setNavigationOnClickListener { alertDialog.show() }
        }
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