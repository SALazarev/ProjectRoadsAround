package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

/** Фабрика ViewModel для фрагмента [ChatFragment].
 * @param chatInteractor - объект управления информацией о сообщениях.
 * @param imageConverter - конвертер изображений.
 */
class ChatViewModelFactory @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val imageConverter: ImageConverter
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return ChatViewModel(chatInteractor, imageConverter) as T
    }
}