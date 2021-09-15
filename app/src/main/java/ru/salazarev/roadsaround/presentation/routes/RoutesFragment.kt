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
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.chat.messagelist.EventListAdapter
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class RoutesFragment : Fragment() {

    private var _binding: FragmentRoutesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var routesViewModelFactory: RoutesViewModelFactory

    private lateinit var viewModel: RoutesViewModel

    private lateinit var adapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(this, routesViewModelFactory).get(RoutesViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setRecyclerView()
        setObserver()
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_routes_menu)
            title = context.getString(R.string.routes)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_messages -> {
                        (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_chatFragment)
                        true
                    }
                    R.id.btn_create_event ->{
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

        adapter = EventListAdapter()
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
            if (listData == null) requireActivity().toast(getString(R.string.сould_not_load_data))
            else adapter.setItems(listData)
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