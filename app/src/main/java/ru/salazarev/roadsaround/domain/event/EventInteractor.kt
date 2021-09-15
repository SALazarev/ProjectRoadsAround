package ru.salazarev.roadsaround.domain.event

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.models.data.EventData
import javax.inject.Inject

class EventInteractor @Inject constructor(private val repository: EventRepository, private val authentication: Authentication) {
    fun createEvent(name: String, note: String, motionType: String, time: Long, route: String): Completable {
        return Completable.fromCallable {
            val authorId = authentication.getUserId()
            repository.sendEvent(EventData(authorId=authorId,name=name,note=note,motionType = motionType,time=time,route = route, members = listOf(authorId)))
        }
    }
    fun getUsersEvents(): Single<List<EventData>>{
        return Single.fromCallable {
        val userId = authentication.getUserId()
        repository.getUserEvents(userId)
            }
    }
}