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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditEventFragment : Fragment() {

    private var _binding: FragmentEditEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditEventViewModel

    @Inject
    lateinit var editEventViewModelFactory: EditEventViewModelFactory


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
        checkArguments()
        setDropDownListMotionTypes()
        binding.btnRoad.setOnClickListener {
            (activity as MainActivity).navController
                .navigate(R.id.action_editEventFragment_to_editRoadFragment)
        }
        binding.btnTime.setOnClickListener { startDatePicker() }
    }

    private fun startDatePicker() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(MaterialDatePicker.todayInUtcMilliseconds())
            .setValidator(DateValidatorPointForward.now())
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setCalendarConstraints(calendarConstraints)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.time = it
            startTimePicker() }
        datePicker.show(childFragmentManager, "TAG")
    }

    private fun startTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .build()
        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.ROOT)
            calendar.time = Date(viewModel.time)
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            requireActivity().toast( dateFormat.format(calendar.time))
            binding.btnTime.setIconResource(R.drawable.outline_done_24)
        }
        timePicker.show(childFragmentManager, "TAG")
    }

    private fun setDropDownListMotionTypes() {
        val items = listOf("Пешком", "Автомобиль", "Велосипед", "Самокат", "Мотоцикл")
        val adapter =
            ArrayAdapter.createFromResource(requireContext(), R.array.motionType, R.layout.item_drop_down_list_motion_type)
        binding.actvMotionType.setAdapter(adapter)
        binding.actvMotionType.setText(items[0],false)
    }

    private fun checkArguments() {
        val bundle = arguments
        if (bundle != null)
            if (bundle.containsKey("ROAD")){
                requireActivity().toast(bundle.getString("ROAD",""))
                binding.btnRoad.setIconResource(R.drawable.outline_done_24)
            }
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
                        (activity as MainActivity).navController
                            .navigate(R.id.action_editEventFragment_to_mainFragment)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun completeEvent() {
        val name = binding.etNameEvent.text.toString().trim().isNotEmpty()

            // viewModel.completeEvent()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}