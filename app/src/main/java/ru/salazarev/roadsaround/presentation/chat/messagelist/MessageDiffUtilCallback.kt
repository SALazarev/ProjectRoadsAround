package ru.salazarev.roadsaround.presentation.chat.messagelist

import androidx.recyclerview.widget.DiffUtil
import ru.salazarev.roadsaround.models.presentation.MessageChat

/** Класс сравнения элементов списка сообщений. */
class MessageDiffUtilCallback(
    private val oldList: List<MessageChat>,
    private val newList: List<MessageChat>
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
        return oldItem.name == newItem.name
                && oldItem.text == newItem.text
                && oldItem.time == newItem.time
    }
}