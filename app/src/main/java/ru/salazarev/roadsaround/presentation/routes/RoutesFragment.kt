package ru.salazarev.roadsaround.presentation.routes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.salazarev.roadsaround.R
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
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_messages -> {
                        (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_chatFragment)
                        true
                    }
                    R.id.btn_create_event ->{
                        (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_editEventFragment)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}