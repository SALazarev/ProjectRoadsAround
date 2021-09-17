package ru.salazarev.roadsaround.presentation.chat.messagelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.ListItemMessageBinding
import ru.salazarev.roadsaround.models.presentation.MessageChat

/** Класс сопоставления отображения сообщений в списке с их данными
 * @param item - отображение сообжения.
 */
class MessageViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: ListItemMessageBinding = ListItemMessageBinding.bind(item)

    /** Установка данных для отображения сообщения.
     * @param data - информация о сообщении.
     */
    fun set(data: MessageChat) {
        binding.apply {
            data.photo?.let { ivUserPhoto.setImageDrawable(it) }
            tvName.text = data.name
            tvText.text = data.text
            tvTime.text = data.time
        }
    }

}