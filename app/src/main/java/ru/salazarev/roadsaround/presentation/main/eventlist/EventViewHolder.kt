package ru.salazarev.roadsaround.presentation.main.eventlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.ListItemEventBinding
import ru.salazarev.roadsaround.models.presentation.EventPreview

/** Класс сопоставления отображения событий в списке с их данными
 * @param item - отображение события.
 */
class EventViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding: ListItemEventBinding = ListItemEventBinding.bind(item)

    /** Установка данных для отображения события.
     * @param data - информация о событии.
     * @param typeWork - тип участия пользователя в событии.
     * @param backgroundColor - цвет фона отображения события.
     */
    fun set(data: EventPreview, typeWork: String,backgroundColor: Int) {
        binding.apply {
            tvAuthor.text = data.authorName
            tvMotionType.text = data.motionType
            tvTimeEvent.text = data.time
            tvNameEvent.text = data.nameEvent
            tvTypeParticipate.text = typeWork
            rvItem.setCardBackgroundColor(backgroundColor)
        }
    }

    /** Установка слушателя нажатие на представление события.*/
    fun setOnClick(clickListener: View.OnClickListener) {
        binding.rvItem.setOnClickListener(clickListener)
    }

}