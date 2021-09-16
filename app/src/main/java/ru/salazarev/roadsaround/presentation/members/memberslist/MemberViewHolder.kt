package ru.salazarev.roadsaround.presentation.members.memberslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.ListItemEventBinding
import ru.salazarev.roadsaround.databinding.ListItemMemberBinding
import ru.salazarev.roadsaround.models.presentation.EventPreview
import ru.salazarev.roadsaround.models.presentation.UserPresentation

class MemberViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: ListItemMemberBinding = ListItemMemberBinding.bind(item)

    fun set(data: UserPresentation) {
        binding.apply {
            tvName.text = data.name
            data.image?.let{ivUserPhoto.setImageDrawable(it)}
        }
    }

}