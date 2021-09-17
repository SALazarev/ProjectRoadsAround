package ru.salazarev.roadsaround.presentation.members

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
import ru.salazarev.roadsaround.databinding.FragmentMembersBinding
import ru.salazarev.roadsaround.presentation.members.memberslist.MembersListAdapter
import ru.salazarev.roadsaround.presentation.main.MainFragment
import javax.inject.Inject

class MembersFragment : Fragment() {
    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MembersListAdapter

    @Inject
    lateinit var membersViewModelFactory: MembersViewModelFactory

    private lateinit var viewModel: MembersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(this, membersViewModelFactory).get(MembersViewModel::class.java)
        binding.rvMembers.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setRecyclerView()
        setObserver()
        val eventId = arguments?.getString(MainFragment.EVENT_ID_KEY) ?: ""
        viewModel.getMembers(eventId)
        binding.viewInformationNotLoad.btnTryAgain.setOnClickListener {
            binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
            binding.rvMembers.visibility = View.VISIBLE
            viewModel.getMembers(eventId)
        }
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_members_menu)
            title = context.getString(R.string.members)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvMembers.layoutManager = layoutManager

        adapter = MembersListAdapter()
        binding.rvMembers.adapter = adapter

        val itemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        binding.rvMembers.addItemDecoration(
            itemDecoration
        )
    }

    private fun setObserver() {

        viewModel.members.observe(viewLifecycleOwner, { members ->
            if (members == null) {
                binding.rvMembers.visibility = View.INVISIBLE
                binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.VISIBLE
            } else {
                adapter.setItems(members)
                binding.rvMembers.visibility = View.VISIBLE
                binding.viewInformationNotLoad.viewInformationNotLoad.visibility = View.INVISIBLE
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