package ru.salazarev.roadsaround.presentation.chat.messagelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.ListItemMessageBinding
import ru.salazarev.roadsaround.models.presentation.MessageChat

class MessageViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: ListItemMessageBinding = ListItemMessageBinding.bind(item)

    fun set(data: MessageChat) {
        binding.apply {
            data.photo?.let { ivUserPhoto.setImageDrawable(it) }
            tvName.text = data.name
            tvText.text = data.text
            tvTime.text = data.time
        }
    }

}