package ru.salazarev.roadsaround.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentChatBinding
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory

    private lateinit var viewModel: ChatViewModel

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
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_chat_menu)
            title = "Тестовый чат"

            binding.rvMessages.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            binding.btnSend.setOnClickListener {
                sendMessage()
            }

            setObserver()
        }
    }

    private fun sendMessage() {
        viewModel.sendMessage(binding.etEnterText.text.toString())
    }

    private fun setObserver() {

        viewModel.result.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) requireActivity().toast("Ок")
            else requireActivity().toast("Не загрузилось")

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