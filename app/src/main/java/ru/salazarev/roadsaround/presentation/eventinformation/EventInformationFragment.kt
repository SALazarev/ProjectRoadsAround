package ru.salazarev.roadsaround.presentation.eventinformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentEventInformationBinding
import ru.salazarev.roadsaround.presentation.editevent.EditEventFragment
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_KEY
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkArguments()
        setObservers()
    }

    private fun setObservers() {
        viewModel.data.observe(viewLifecycleOwner,{
          //  binding.etNameAuthor.setText(it.members.find { it. })
        })
        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })
    }

    private fun checkArguments() {
        val bundle = arguments
        if (bundle != null) {
            val eventId = bundle.getString(EVENT_KEY)
            if (eventId != null) {
                viewModel.getEventData(eventId)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}