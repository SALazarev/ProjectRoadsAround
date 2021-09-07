package ru.salazarev.roadsaround.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.databinding.FragmentChatBinding
import ru.salazarev.roadsaround.databinding.FragmentMainBinding
import ru.salazarev.roadsaround.di.DaggerAppComponent
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.profile.ProfileViewModel
import ru.salazarev.roadsaround.presentation.profile.ProfileViewModelFactory
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

        DaggerAppComponent.builder().fragmentManager(childFragmentManager).build().inject(this)

        viewModel =
            ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_chat_menu)
            title = "Тестовый чат"

//            binding.rvMessages.layoutManager =
//                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

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
        viewModel.errors.observe(requireActivity(),{
            requireActivity().toast("Произошла ошибка")
        })

        viewModel.userLiveData.observe(requireActivity(),{
            requireActivity().toast("Всё ок")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}