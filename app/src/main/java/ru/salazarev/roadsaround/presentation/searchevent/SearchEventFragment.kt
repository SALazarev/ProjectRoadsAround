package ru.salazarev.roadsaround.presentation.searchevent

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
import ru.salazarev.roadsaround.databinding.FragmentSearchEventBinding
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.main.eventlist.EventListAdapter
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_ID_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_NAME_KEY
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_TYPE_WORK_KEY
import ru.salazarev.roadsaround.presentation.main.eventlist.ClickItemCallback
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class SearchEventFragment : Fragment() {

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var searchEventViewModelFactory: SearchEventViewModelFactory

    private var _binding: FragmentSearchEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventListAdapter

    private lateinit var viewModel: SearchEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel = ViewModelProvider(
            this, searchEventViewModelFactory
        ).get(SearchEventViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchEventBinding.inflate(inflater, container, false)
        setProgressColor()
        startLoadEvents()
        return binding.root
    }

    private fun setProgressColor() {
        val progressColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        binding.layoutSwipeRefresh.setColorSchemeColors(progressColor)
    }

    private fun startLoadEvents() {
        binding.rvEvent.visibility = View.INVISIBLE
        viewModel.loadUsersEventsList()
        binding.layoutSwipeRefresh.isRefreshing = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setRecyclerView()
        setObserver()
        binding.viewInformationNotLoad.btnTryAgain.setOnClickListener {
            binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
            viewModel.loadUsersEventsList()
        }
        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.loadUsersEventsList()
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
                bundle.putString(EVENT_TYPE_WORK_KEY, typeWorkWithEvent.name)
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

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_search_menu)
            title = context.getString(R.string.search)
        }
    }

    private fun setObserver() {

        viewModel.eventsLiveData.observe(viewLifecycleOwner, { listData ->
            binding.layoutSwipeRefresh.isRefreshing = false
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
                    binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
                    binding.rvEvent.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}