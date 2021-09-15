package ru.salazarev.roadsaround.presentation.eventinformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.databinding.FragmentEventInformationBinding
import javax.inject.Inject

class EventInformationFragment : Fragment() {

    private var _binding: FragmentEventInformationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var eventInformationViewModelFactory: EventInformationViewModelFactory

    private lateinit var viewModel: EventInformationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventInformationBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(
                this,
                eventInformationViewModelFactory
            ).get(EventInformationViewModel::class.java)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}