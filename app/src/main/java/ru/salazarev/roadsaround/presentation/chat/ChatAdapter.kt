package ru.salazarev.roadsaround.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.models.domain.Message

class ChatAdapter(val data: List<Message>): RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_item_message,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
       holder.set(data[position])
    }

    override fun getItemCount(): Int = data.size
}
