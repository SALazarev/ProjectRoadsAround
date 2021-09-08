package ru.salazarev.roadsaround.presentation.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.RvItemMessageBinding
import ru.salazarev.roadsaround.models.domain.Message

class MessageViewHolder(item: View): RecyclerView.ViewHolder(item){
    private val binding: RvItemMessageBinding = RvItemMessageBinding.bind(item)

    fun set(data: Message) {
        binding.apply {
            ivUserPhoto.background = data.photo
            tvName.text = data.name
            tvMessage.text = data.message
            tvTime.text = data.time
        }
    }

}