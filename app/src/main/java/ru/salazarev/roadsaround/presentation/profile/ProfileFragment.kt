package ru.salazarev.roadsaround.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentProfileBinding
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.MainActivity
import javax.inject.Inject

class ProfileFragment : Fragment() {

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val alertDialog: AlertDialog by lazy { createAlertDialog() }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.mainLayout.visibility = View.VISIBLE
        viewModel.loadUserData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setObserver()
        binding.viewInformationNotLoad.btnTryAgain.setOnClickListener {
            binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
            binding.mainLayout.visibility = View.VISIBLE
            viewModel.loadUserData()
        }
    }

    private fun setObserver() {

        viewModel.user.observe(viewLifecycleOwner, { user ->
            if (user == null) {
                binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.INVISIBLE
            } else {
                setViewData(user)
                binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
                binding.mainLayout.visibility = View.VISIBLE
            }
        })

        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })
    }

    private fun setViewData(user: UserPresentation) {
        val userName = "${resources.getString(R.string.name)}: ${user.name}"
        binding.etName.text = userName
        if (user.image == null) binding.ivUserPhoto.visibility = View.GONE else
            binding.ivUserPhoto.setImageDrawable(user.image)
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
        val builder = MaterialAlertDialogBuilder(requireContext())
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