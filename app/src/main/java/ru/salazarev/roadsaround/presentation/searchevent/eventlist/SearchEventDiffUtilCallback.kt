package ru.salazarev.roadsaround.presentation.chat.messagelist

import androidx.recyclerview.widget.DiffUtil
import ru.salazarev.roadsaround.models.presentation.EventPreview

class SearchEventDiffUtilCallback(
    private val oldList: List<EventPreview>,
    private val newList: List<EventPreview>
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