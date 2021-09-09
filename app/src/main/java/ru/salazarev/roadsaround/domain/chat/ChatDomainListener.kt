package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.models.domain.Message

interface ChatDomainListener {
    fun getData(data: List<Message>)
}