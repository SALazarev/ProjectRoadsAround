package ru.salazarev.roadsaround.domain.chat

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.data.MessageData
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userInteractor: UserInteractor,
    private val auth: Authentication
) {


    fun sendMessage(textMessage: String) {
        chatRepository.sendMessage(auth.getUserId(), textMessage)
    }

    fun getChatMessages(callback: PublishSubject<List<Message>>) {
        val localCallback = PublishSubject.create<List<MessageData>>()
        localCallback.subscribe({ list ->
            val single: Single<User> = Single.fromCallable {
                return@fromCallable userInteractor.getUserData()
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            single.subscribe({callback.onNext(getData(list,it))}){}
        }){}
        chatRepository.getChatMessages(localCallback)
    }

    fun getData(data: List<MessageData>, user: User): List<Message> {
        return data.map {
            val name =
                if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
            Message(
                it.id,
                it.authorId,
                name,
                it.text,
                it.time!!.toDate().toString(),
                user.image
            )
        }
    }

    fun test(source: PublishSubject<String>){
        val sss = PublishSubject.create<String>()
            sss.subscribe({ name ->
                val single: Single<User> = Single.fromCallable {
                    return@fromCallable userInteractor.getUserData()
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                single.subscribe({source.onNext("$name +${it.firstName}")}) { Log.d("TAG","Беда") }
            }){}
        chatRepository.test(sss)
    }
}