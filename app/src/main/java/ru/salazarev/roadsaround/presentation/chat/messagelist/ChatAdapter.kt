package ru.salazarev.roadsaround.presentation.chat.messagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.models.presentation.MessageChat

/** Адаптер для списка сообщений.
 * @param data - список сообщений.
 */
class ChatAdapter(var data: List<MessageChat> = mutableListOf()) :
    RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_message,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.set(data[position])
    }

    override fun getItemCount(): Int = data.size

    /** Установка списка сообщений для адаптера.
     * @param items - список сообщений.
     */
    fun setItems(items: List<MessageChat>) {
        val diffResult = DiffUtil.calculateDiff(
            MessageDiffUtilCallback(
                data,
                items
            )
        )
        data = items
        diffResult.dispatchUpdatesTo(this)
    }


}
