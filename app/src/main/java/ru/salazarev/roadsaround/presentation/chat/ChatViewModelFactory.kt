package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.domain.user.UserInteractor
import javax.inject.Inject

class ChatViewModelFactory @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val userInteractor: UserInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return ChatViewModel(chatInteractor, userInteractor) as T
    }
}