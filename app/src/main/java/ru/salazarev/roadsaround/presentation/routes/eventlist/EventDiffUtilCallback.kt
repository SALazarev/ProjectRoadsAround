package ru.salazarev.roadsaround.presentation.chat.messagelist

import androidx.recyclerview.widget.DiffUtil
import ru.salazarev.roadsaround.models.presentation.Event
import ru.salazarev.roadsaround.models.presentation.MessageChat

class EventDiffUtilCallback(
    private val oldList: List<Event>,
    private val newList: List<Event>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.nameEvent == newItem.nameEvent
                && oldItem.authorName == newItem.authorName
                && oldItem.time == newItem.time
                && oldItem.motionType == newItem.motionType
    }
}