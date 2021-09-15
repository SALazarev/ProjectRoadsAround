package ru.salazarev.roadsaround.presentation.eventinformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentEventInformationBinding
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_ID_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.NAME_EVENT_KEY
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
    ): View {
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
        configureToolbar()
        checkArguments()
        setObservers()
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_event_information)
            title = arguments?.getString(NAME_EVENT_KEY) ?: context.getString(R.string.event)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_complete_edit_event -> {
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun checkArguments() {
        val bundle = arguments
        if (bundle != null) {
            val eventId = bundle.getString(EVENT_ID_KEY)
            if (eventId != null) {
                viewModel.getEventData(eventId)
            }
        }
    }

    private fun setObservers() {
        viewModel.data.observe(viewLifecycleOwner, { event ->
            val user = event.members.find { it.id == event.authorId }!!
            val nameAuthor =
                if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
            binding.etNameAuthor.setText(nameAuthor)
            binding.etMotionType.setText(event.motionType)
            binding.etTime.setText(event.time)
           if(event.note.isNotEmpty()) binding.etDescription.setText(event.note) else binding.tilDescription.visibility = View.GONE
        })
        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}