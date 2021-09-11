package ru.salazarev.roadsaround.presentation.editroad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentEditRoadBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import javax.inject.Inject

class EditRoadFragment : Fragment() {

    private var _binding: FragmentEditRoadBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditRoadViewModel

    @Inject
    lateinit var editRoadViewModelFactory: EditRoadViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentEditRoadBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)
        viewModel =
            ViewModelProvider(this, editRoadViewModelFactory).get(EditRoadViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_edit_road_menu)
            title = context.getString(R.string.settings_route)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener {
                (activity as MainActivity).navController
                    .navigate(R.id.action_editRoadFragment_to_editEventFragment)
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_complete_edit_road -> {
                        val bundle = Bundle().apply {
                            putString("ROAD","POINTS")
                        }
                        (activity as MainActivity).navController
                            .navigate(R.id.action_editRoadFragment_to_editEventFragment, bundle)
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