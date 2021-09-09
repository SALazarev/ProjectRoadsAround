package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.models.data.MessageData

interface ChatRepoListener {
    fun getData(data: List<MessageData>)
}