package ru.salazarev.roadsaround.presentation.routes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentRoutesBinding
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.main.eventlist.EventListAdapter
import ru.salazarev.roadsaround.presentation.main.MainFragment
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_ID_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_NAME_KEY
import ru.salazarev.roadsaround.presentation.main.eventlist.ClickItemCallback
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class RoutesFragment : Fragment() {

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var routesViewModelFactory: RoutesViewModelFactory

    private var _binding: FragmentRoutesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RoutesViewModel

    private lateinit var adapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(this, routesViewModelFactory).get(RoutesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        binding.rvEvent.visibility = View.INVISIBLE
        viewModel.loadUserEvents()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setRecyclerView()
        setObserver()
        binding.viewInformationNotLoad.btnTryAgain.setOnClickListener {
            binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
            viewModel.loadUserEvents()
        }
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_routes_menu)
            title = context.getString(R.string.my_events)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_create_event -> {
                        (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_editEventFragment)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvEvent.layoutManager = layoutManager

        adapter = EventListAdapter(object : ClickItemCallback {
            override fun onClick(
                id: String,
                name: String,
                typeWorkWithEvent: EventInteractor.Companion.TypeWorkWithEvent
            ) {
                val bundle = Bundle()
                bundle.putString(EVENT_ID_KEY, id)
                bundle.putString(EVENT_NAME_KEY, name)
                bundle.putString(MainFragment.EVENT_TYPE_WORK_KEY, typeWorkWithEvent.name)
                (activity as MainActivity).navController.navigate(
                    R.id.action_mainFragment_to_eventInformationFragment,
                    bundle
                )
            }

        })
        binding.rvEvent.adapter = adapter

        val itemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rv_divider
            )!!
        )
        binding.rvEvent.addItemDecoration(
            itemDecoration
        )
    }

    private fun setObserver() {
        viewModel.eventsLiveData.observe(viewLifecycleOwner, { listData ->
            when {
                listData == null -> {
                    binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.VISIBLE
                    binding.rvEvent.visibility = View.INVISIBLE
                }
                listData.isEmpty() -> {
                    requireActivity().toast(getString(R.string.no_events))
                    binding.rvEvent.visibility = View.INVISIBLE
                }
                else -> {
                    adapter.setItems(listData)
                    binding.viewInformationNotLoad.viewInformationNotLoad.visibility =
                        View.INVISIBLE
                    binding.rvEvent.visibility = View.VISIBLE
                }
            }
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