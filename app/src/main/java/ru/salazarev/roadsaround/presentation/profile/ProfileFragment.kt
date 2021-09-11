package ru.salazarev.roadsaround.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentProfileBinding
import ru.salazarev.roadsaround.models.presentation.UserChat
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class ProfileFragment : Fragment() {


    private val alertDialog: AlertDialog by lazy { createAlertDialog() }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setObserver()
    }

    private fun setObserver() {

        viewModel.userLiveData.observe(viewLifecycleOwner, { user ->
            if (user == null) requireActivity().toast(getString(R.string.сould_not_load_data))
            else setViewData(user)
        })

        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })
    }

    private fun setViewData(user: UserChat) {
        binding.etFirstName.text = user.firstName
        binding.etLastName.text = user.lastName
        if (user.image != null) binding.btnUserPhoto.background = user.image
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_profile_menu)
            title = context.getString(R.string.profile)
            navigationContentDescription = context.getString(R.string.cancel_from_profile)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_logout_24)
            setNavigationOnClickListener { alertDialog.show() }
        }
    }

    private fun createAlertDialog(): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.you_shure_log_out))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ -> logout() }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
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