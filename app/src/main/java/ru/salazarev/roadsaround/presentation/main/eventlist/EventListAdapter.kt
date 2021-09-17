package ru.salazarev.roadsaround.presentation.main.eventlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.presentation.EventPreview
import ru.salazarev.roadsaround.presentation.chat.messagelist.EventDiffUtilCallback

class EventListAdapter(
    private val callback: ClickItemCallback,
    var data: List<EventPreview> = mutableListOf()
) : RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_event,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = data[position]

        val context = holder.itemView.context
        val resource =  context.resources

        val typeWork = when (item.typeWorkWithEvent){
            EventInteractor.Companion.TypeWorkWithEvent.GUEST -> ""
            EventInteractor.Companion.TypeWorkWithEvent.AUTHOR -> resource.getString(R.string.organizer)
            EventInteractor.Companion.TypeWorkWithEvent.MEMBER -> resource.getString(R.string.member)
        }

        val backgroundColor = when (item.typeWorkWithEvent){
            EventInteractor.Companion.TypeWorkWithEvent.GUEST ->  ContextCompat.getColor(holder.itemView.context,R.color.colorAccent)
            EventInteractor.Companion.TypeWorkWithEvent.AUTHOR -> ContextCompat.getColor(holder.itemView.context,R.color.design_default_color_primary_dark)
            EventInteractor.Companion.TypeWorkWithEvent.MEMBER -> ContextCompat.getColor(holder.itemView.context,R.color.design_default_color_primary)
        }

        holder.set(item,typeWork,backgroundColor)
        holder.setOnClick { callback.onClick(item.id, item.nameEvent, item.typeWorkWithEvent) }
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
