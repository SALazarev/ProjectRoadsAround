package ru.salazarev.roadsaround.presentation.chat.messagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.models.presentation.EventPreview
import ru.salazarev.roadsaround.presentation.main.eventlist.ClickItemCallback
import ru.salazarev.roadsaround.presentation.main.eventlist.EventViewHolder

class EventListAdapter(val callback: ClickItemCallback, var data: List<EventPreview> = mutableListOf()) : RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_item_event,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.set(data[position])
        holder.setOnClick { callback.onClick(data[position].id) }
    }

    override fun getItemCount(): Int = data.size

    fun setItems(items: List<EventPreview>) {
        val diffResult = DiffUtil.calculateDiff(
            EventDiffUtilCallback(
                data,
                items
            )
        )
        data = items
        diffResult.dispatchUpdatesTo(this)
    }


}
