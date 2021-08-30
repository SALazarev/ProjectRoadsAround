package ru.salazarev.roadsaround.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentRegBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast

class RegFragment : Fragment() {

    private var _binding: FragmentRegBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            inflateMenu(R.menu.toolbar_reg_menu)
            title = context.getString(R.string.registration)
            navigationIcon = ContextCompat.getDrawable(context,R.drawable.outline_arrow_back_white_24)
            setNavigationOnClickListener { (activity as MainActivity).navController.navigate(R.id.action_regFragment_to_authFragment) }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_login -> {
                        registration()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun registration() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPass.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            context?.toast(getString(R.string.required_data_fields_empty))
            return
        } else {
            (activity as MainActivity).fireBaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        context?.toast(getString(R.string.reg_successful))
                        (activity as MainActivity).navController.navigate(R.id.action_regFragment_to_mainFragment)
                    } else {
                        context?.toast(getString(R.string.reg_unsuccessful))
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}