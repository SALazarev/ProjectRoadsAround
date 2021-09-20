package ru.salazarev.roadsaround.presentation.main.eventlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.ListItemEventBinding
import ru.salazarev.roadsaround.models.presentation.EventPreview
import java.text.SimpleDateFormat
import java.util.*

/** Класс сопоставления отображения событий в списке с их данными
 * @param item - отображение события.
 */
class EventViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    companion object {
        private const val FORMAT_DATE = "HH:mm dd/MM/yyyy"
    }

    private val binding: ListItemEventBinding = ListItemEventBinding.bind(item)

    /** Установка данных для отображения события.
     * @param data - информация о событии.
     * @param typeWork - тип участия пользователя в событии.
     * @param backgroundColor - цвет фона отображения события.
     */
    fun set(data: EventPreview, typeWork: String, backgroundColor: Int) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(FORMAT_DATE, Locale.ROOT)
        calendar.time = Date(data.time)

        binding.apply {
            tvAuthor.text = data.authorName
            tvMotionType.text = data.motionType
            tvTimeEvent.text = dateFormat.format(calendar.time)
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