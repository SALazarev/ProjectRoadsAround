package ru.salazarev.roadsaround.presentation.chat.messagelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.RvItemEventBinding
import ru.salazarev.roadsaround.databinding.RvItemMessageBinding
import ru.salazarev.roadsaround.models.presentation.Event
import ru.salazarev.roadsaround.models.presentation.MessageChat

class EventViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: RvItemEventBinding = RvItemEventBinding.bind(item)

    fun set(data: Event) {
        binding.apply {
            tvAuthor.text = data.authorName
            tvMotionType.text = data.motionType
            tvTimeEvent.text = data.time
            tvNameEvent.text = data.nameEvent
        }
    }

}