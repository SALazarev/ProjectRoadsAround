package ru.salazarev.roadsaround.presentation.members.memberslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.models.presentation.UserPresentation

class MembersListAdapter(
    var data: List<UserPresentation> = mutableListOf()
) : RecyclerView.Adapter<MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_member,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val item = data[position]
        holder.set(item)
    }

    override fun getItemCount(): Int = data.size

    fun setItems(items: List<UserPresentation>) {
        data = items
        notifyDataSetChanged()
    }


}
