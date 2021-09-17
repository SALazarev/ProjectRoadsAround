package ru.salazarev.roadsaround.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentChatBinding
import ru.salazarev.roadsaround.presentation.chat.messagelist.ChatAdapter
import ru.salazarev.roadsaround.presentation.main.MainFragment.Companion.EVENT_ID_KEY
import ru.salazarev.roadsaround.toast
import javax.inject.Inject


class ChatFragment : Fragment() {

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel

    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)

        viewModel =
            ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        configureToolbar()
        setObserver()
        binding.btnSend.setOnClickListener { sendMessage() }
        val eventId = arguments?.getString(EVENT_ID_KEY) ?: ""
        viewModel.getMessages(eventId)
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.rvMessages.layoutManager = layoutManager

        adapter = ChatAdapter()
        binding.rvMessages.adapter = adapter

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
        binding.rvMessages.addItemDecoration(
            itemDecoration
        )
    }

    private fun configureToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_chat_menu)
            title = context.getString(R.string.chat)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun sendMessage() {
        if (binding.etEnterText.text.toString().trim().isNotEmpty()) {
            val eventId = arguments?.getString(EVENT_ID_KEY) ?: ""
            viewModel.sendMessage(
                eventId,
                binding.etEnterText.text.toString()
            )
            binding.etEnterText.setText("")
        } else requireActivity().toast(getString(R.string.incorrectly_entered_text))
    }

    private fun setObserver() {
        viewModel.result.observe(viewLifecycleOwner, { result ->
            if (!result) requireActivity().toast(getString(R.string.message_load_unsuccess))
        })
        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })

        viewModel.messages.observe(viewLifecycleOwner, { messages ->
            adapter.setItems(messages)
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}