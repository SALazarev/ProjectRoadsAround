package ru.salazarev.roadsaround.presentation.authentification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentAuthBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class AuthFragment : Fragment() {

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)
        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvRegistration.setOnClickListener { registration() }
        binding.tvRestorePassword.setOnClickListener { passwordReset() }

        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_auth_menu)
            title = context.getString(R.string.authentication)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_login -> {
                        authentication()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
        setObservable()
    }

    private fun setObservable() {
        viewModel.authStatus.observe(viewLifecycleOwner, { authStatus ->
            if (authStatus) {
                (activity as MainActivity).navController
                    .navigate(R.id.action_authFragment_to_mainFragment)
            } else {
                requireActivity().toast(getString(R.string.auth_unsuccessful))
            }
        })
        viewModel.resetPassStatus.observe(viewLifecycleOwner, { resetStatus ->
            if (resetStatus) {
                context?.toast(getString(R.string.mail_sent))
            } else {
                context?.toast(getString(R.string.restore_pass_unsuccessfull))
            }
        })
        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })
    }

    private fun passwordReset() {
        val email = binding.etEmail.editText!!.text.toString()
        if (email.isEmpty()) context?.toast(getString(R.string.enter_email_to_restore_password))
        else viewModel.resetPassword(email)
    }


    private fun authentication() {
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPass.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            context?.toast(getString(R.string.auth_fields_empty))
        } else viewModel.authenticationUser(email, password)
    }

    private fun registration() {
        (activity as MainActivity).navController.navigate(R.id.action_authFragment_to_regFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}