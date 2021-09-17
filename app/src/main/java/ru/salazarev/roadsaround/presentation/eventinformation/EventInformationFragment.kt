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
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.editevent.EditEventFragment.Companion.ROUTE_KEY
import ru.salazarev.roadsaround.presentation.editroad.EditRoadFragment
import ru.salazarev.roadsaround.presentation.editroad.EditRoadFragment.Companion.EDIT_ROAD_TYPE_WORK_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_ID_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_NAME_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_TYPE_WORK_KEY
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class EventInformationFragment : Fragment() {

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var eventInformationViewModelFactory: EventInformationViewModelFactory

    private var _binding: FragmentEventInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventInformationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel = ViewModelProvider(
            this, eventInformationViewModelFactory
        ).get(EventInformationViewModel::class.java)

        arguments?.getString(EVENT_ID_KEY)?.let { viewModel.getEventData(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventInformationBinding.inflate(inflater, container, false)
        binding.layoutMain.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureFragmentByTypeWork()
        setObservers()

        binding.viewInformationNotLoad.btnTryAgain.setOnClickListener {
            binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
            arguments?.getString(EVENT_ID_KEY)?.let { viewModel.getEventData(it) }
        }
        binding.btnMembers.setOnClickListener {
            val bundle = Bundle()
            val idEvent = arguments?.getString(EVENT_ID_KEY)
            bundle.putString(EVENT_ID_KEY, idEvent)
            (activity as MainActivity).navController.navigate(
                R.id.action_eventInformationFragment_to_membersFragment, bundle
            )
        }
        binding.btnRoad.setOnClickListener {
            val bundle = Bundle()
            viewModel.data.value?.let { bundle.putString(ROUTE_KEY, it.route) }
            bundle.putString(
                EDIT_ROAD_TYPE_WORK_KEY,
                EditRoadFragment.Companion.EditRoadTypeWork.VIEW.name
            )
            (activity as MainActivity).navController.navigate(
                R.id.action_eventInformationFragment_to_editRoadFragment, bundle
            )
        }
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_event_information)
            title = arguments?.getString(EVENT_NAME_KEY) ?: context.getString(R.string.event)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_chat -> {
                        val bundle = Bundle()
                        bundle.putString(EVENT_ID_KEY, arguments?.getString(EVENT_ID_KEY))
                        (activity as MainActivity).navController.navigate(
                            R.id.action_eventInformationFragment_to_chatFragment,
                            bundle
                        )
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun configureFragmentByTypeWork() {
        arguments?.let { bundle ->
            bundle.getString(EVENT_TYPE_WORK_KEY)?.let { it ->
                val typeWork = when (it) {
                    EventInteractor.Companion.TypeWorkWithEvent.AUTHOR.name -> EventInteractor.Companion.TypeWorkWithEvent.AUTHOR
                    EventInteractor.Companion.TypeWorkWithEvent.MEMBER.name -> EventInteractor.Companion.TypeWorkWithEvent.MEMBER
                    EventInteractor.Companion.TypeWorkWithEvent.GUEST.name -> EventInteractor.Companion.TypeWorkWithEvent.GUEST
                    else -> EventInteractor.Companion.TypeWorkWithEvent.GUEST
                }
                configureTypeWorkWithFragment(typeWork)
            }
        }
    }

    private fun configureTypeWorkWithFragment(typeWork: EventInteractor.Companion.TypeWorkWithEvent) {
        when (typeWork) {
            EventInteractor.Companion.TypeWorkWithEvent.AUTHOR -> setAuthorMode()
            EventInteractor.Companion.TypeWorkWithEvent.MEMBER -> setMemberMode()
            EventInteractor.Companion.TypeWorkWithEvent.GUEST -> setGuestMode()
        }
    }

    private fun setMemberMode() {
        binding.includeToolbar.includeToolbar.menu.findItem(R.id.btn_chat).isVisible = true
        binding.btnParticipate.apply {
            text = context.getString(R.string.leave_from_event)
            val eventId = arguments?.getString(EVENT_ID_KEY) ?: ""
            setOnClickListener {
                viewModel.leaveFromEvent(eventId)
            }
        }
    }

    private fun setAuthorMode() {
        binding.includeToolbar.includeToolbar.menu.findItem(R.id.btn_chat).isVisible = true
        binding.btnParticipate.apply {
            text = context.getString(R.string.edit_event)
            setOnClickListener {
                val bundle = Bundle()
                bundle.putString(EVENT_ID_KEY, arguments?.getString(EVENT_ID_KEY))
                (activity as MainActivity).navController.navigate(
                    R.id.action_eventInformationFragment_to_editEventFragment, bundle
                )
            }
        }
    }


    private fun setGuestMode() {
        binding.includeToolbar.includeToolbar.menu.findItem(R.id.btn_chat).isVisible = false
        binding.btnParticipate.apply {
            text = context.getString(R.string.participate)
            val eventId = arguments?.getString(EVENT_ID_KEY) ?: ""
            setOnClickListener {
                viewModel.participateFromEvent(eventId)
            }
        }
    }


    private fun setObservers() {

        viewModel.data.observe(viewLifecycleOwner, { event ->
            if (event == null) {
                binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.VISIBLE
                binding.layoutMain.visibility = View.INVISIBLE
            } else {
                val user = event.members.find { it.id == event.authorId }!!
                binding.etNameAuthor.setText(user.name)
                binding.etMotionType.setText(event.motionType)
                binding.etTime.setText(event.time)
                if (event.note.isNotEmpty()) binding.etDescription.setText(event.note)
                else binding.tilDescription.visibility = View.GONE
                binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
                binding.layoutMain.visibility = View.VISIBLE
            }
        })

        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })

        viewModel.resultParticipate.observe(viewLifecycleOwner, { result ->
            if (result) setMemberMode()
            else requireActivity().toast(resources.getString(R.string.failed_load_data))
        })

        viewModel.resultLeave.observe(viewLifecycleOwner, { result ->
            if (result) setGuestMode()
            else requireActivity().toast(resources.getString(R.string.failed_load_data))
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
