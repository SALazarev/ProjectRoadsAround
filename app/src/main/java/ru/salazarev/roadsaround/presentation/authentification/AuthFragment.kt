package ru.salazarev.roadsaround.presentation.authentification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentAuthBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

    private fun passwordReset() {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) context?.toast(getString(R.string.enter_email_to_restore_password))
        else {
            (activity as MainActivity).fireBaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) context?.toast(getString(R.string.mail_sent))
                }
        }
    }


    private fun authentication() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            context?.toast(getString(R.string.auth_fields_empty))
            return
        } else {
            (activity as MainActivity).fireBaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        context?.toast(getString(R.string.auth_successful))
                        (activity as MainActivity).navController.navigate(R.id.action_authFragment_to_mainFragment)
                    } else {
                        context?.toast(getString(R.string.auth_not_success))
                    }
                }
        }
    }

    private fun registration() {
        (activity as MainActivity).navController.navigate(R.id.action_authFragment_to_regFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}