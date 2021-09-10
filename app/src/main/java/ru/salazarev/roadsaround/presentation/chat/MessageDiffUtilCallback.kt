package ru.salazarev.roadsaround.presentation.chat

import androidx.recyclerview.widget.DiffUtil
import ru.salazarev.roadsaround.models.presentation.MessageChat

class MessageDiffUtilCallback(private val oldList: List<MessageChat>,
                              private val newList: List<MessageChat>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldElement = oldList[oldItemPosition]
        val newElement = newList[newItemPosition]
        val oldId: String = oldElement.id
        val newId: String = newElement.id
        return oldId == newId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.name == newItem.name
                && oldItem.message == newItem.message
                && oldItem.time == newItem.time
    }
}