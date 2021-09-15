package ru.salazarev.roadsaround.presentation.main.eventlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.RvItemEventBinding
import ru.salazarev.roadsaround.models.presentation.EventPreview

class EventViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: RvItemEventBinding = RvItemEventBinding.bind(item)

    fun set(data: EventPreview) {
        binding.apply {
            tvAuthor.text = data.authorName
            tvMotionType.text = data.motionType
            tvTimeEvent.text = data.time
            tvNameEvent.text = data.nameEvent
        }
    }

    fun setOnClick(clickListener: View.OnClickListener) {
        binding.rvItem.setOnClickListener(clickListener)
    }

}