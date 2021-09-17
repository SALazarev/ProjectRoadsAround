package ru.salazarev.roadsaround.presentation.members.memberslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.databinding.ListItemMemberBinding
import ru.salazarev.roadsaround.models.presentation.UserPresentation

/** Класс сопоставления отображения участников события в списке с их данными
 * @param item - отображение участника в событии.
 */
class MemberViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: ListItemMemberBinding = ListItemMemberBinding.bind(item)

    /** Установка данных для отображения участника события.
     * @param data - информация об участнике события.
     */
    fun set(data: UserPresentation) {
        binding.apply {
            tvName.text = data.name
            data.image?.let { ivUserPhoto.setImageDrawable(it) }
        }
    }

}