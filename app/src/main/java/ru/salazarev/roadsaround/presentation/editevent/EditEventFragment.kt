package ru.salazarev.roadsaround.presentation.editevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentEditEventBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast
import java.util.*
import javax.inject.Inject


class EditEventFragment : Fragment() {

    companion object {
        const val PICKERS_TAG = "TAG"
        const val ROUTE_KEY = "ROUTE_KEY"
        const val ROUTE_REQUEST = "ROUTE_REQUEST"
    }

    private var _binding: FragmentEditEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditEventViewModel

    @Inject
    lateinit var editEventViewModelFactory: EditEventViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(ROUTE_REQUEST,this){key, bundle ->
            val route = bundle.getString(ROUTE_KEY)
           viewModel.setRoute(route)
            if(viewModel.getRoute()!=null) binding.btnRoad.setIconResource(R.drawable.outline_done_24)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditEventBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)
        viewModel =
            ViewModelProvider(this, editEventViewModelFactory).get(EditEventViewModel::class.java)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setDropDownListMotionTypes()
        if(viewModel.getRoute()!=null) binding.btnRoad.setIconResource(R.drawable.outline_done_24)
       if(viewModel.getTime()!=null) binding.btnTime.setIconResource(R.drawable.outline_done_24)


        binding.btnRoad.setOnClickListener {
            val bundle = Bundle()
            val route = viewModel.getRoute()
            if (route != null) bundle.putString(ROUTE_KEY, route)
            (activity as MainActivity).navController
                .navigate(R.id.action_editEventFragment_to_editRoadFragment, bundle)
        }
        binding.btnTime.setOnClickListener { startDatePicker() }

        viewModel.result.observe(viewLifecycleOwner,{
            result ->
            if (result) (activity as MainActivity).navController
                .navigate(R.id.action_editEventFragment_to_mainFragment)
            else requireActivity().toast("PROBLEM")
        })
    }

    private fun startDatePicker() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(MaterialDatePicker.todayInUtcMilliseconds())
            .setValidator(DateValidatorPointForward.now())
            .build()

        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setCalendarConstraints(calendarConstraints)

        viewModel.getTime()?.let { builder.setSelection(it) }
        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener {
            startTimePicker(it)
        }
        datePicker.show(childFragmentManager, PICKERS_TAG)
    }

    private fun startTimePicker(timeOfDatePicker: Long) {
        val time = viewModel.getTime() ?: timeOfDatePicker
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        val builder =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))

        val timePicker = builder.build()
        timePicker.addOnPositiveButtonClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            viewModel.setTime(calendar.timeInMillis)
            binding.btnTime.setIconResource(R.drawable.outline_done_24)
        }
        timePicker.show(childFragmentManager, PICKERS_TAG)
    }

    private fun setDropDownListMotionTypes() {
        val motions = resources.getStringArray(R.array.motionType)
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_drop_down_list_motion_type, motions)
        binding.actvMotionType.setAdapter(adapter)
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_edit_event_menu)
            title = context.getString(R.string.event_creation)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener {
                (activity as MainActivity).navController
                    .navigate(R.id.action_editEventFragment_to_mainFragment)
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_complete_edit_event -> {
                        completeEvent()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }


    private fun completeEvent() {
        val name = binding.etNameEvent.text.toString()
        val note = binding.etDescription.text.toString()
        val motionType = binding.actvMotionType.text.toString()
        val time = viewModel.getTime()
        val route = viewModel.getRoute()

        if (name.trim().isNotEmpty() && motionType.isNotEmpty() && time!=null && route!=null)
            viewModel.createEvent(name,note,motionType,time,route)
        else requireActivity().toast(getString(R.string.not_all_necessary_data))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}