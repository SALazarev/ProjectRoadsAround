package ru.salazarev.roadsaround.presentation.chat.messagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.models.presentation.Event

class SearchEventListAdapter(var data: List<Event> = mutableListOf()) : RecyclerView.Adapter<SearchEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchEventViewHolder {
        return SearchEventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_item_event,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchEventViewHolder, position: Int) {
        holder.set(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setItems(items: List<Event>) {
        val diffResult = DiffUtil.calculateDiff(
            SearchEventDiffUtilCallback(
                data,
                items
            )
        )
        data = items
        diffResult.dispatchUpdatesTo(this)
    }


}
