package ru.salazarev.roadsaround.presentation.searchevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ui.setupWithNavController
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentProfileBinding
import ru.salazarev.roadsaround.databinding.FragmentSearchEventBinding
import ru.salazarev.roadsaround.presentation.MainActivity

class SearchEventFragment : Fragment() {

    private var _binding: FragmentSearchEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNav.bottomNav.apply {
            setupWithNavController((activity as MainActivity).navController)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}