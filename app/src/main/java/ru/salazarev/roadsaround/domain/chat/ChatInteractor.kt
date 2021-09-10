package ru.salazarev.roadsaround.domain.chat

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.data.MessageData
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.UserChat
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userInteractor: UserInteractor,
    private val auth: Authentication
) {


    fun sendMessage(textMessage: String) {
        chatRepository.sendMessage(auth.getUserId(), textMessage)
    }

    fun getChatMessages(callback: ChatDomainListener) {
        chatRepository.getChatMessages(object : ChatRepoListener {
            override fun getData(data: List<MessageData>) {
                val newData: List<Message> = data.map {
                    val user = userInteractor.getUserData(it.id)
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
                callback.getData(newData)
            }
        })
    }

    fun test(source: PublishSubject<String>){
        val sss = PublishSubject.create<String>()
            sss.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            sss.subscribe({
                val single: Single<User> = Single.fromCallable {
                    return@fromCallable userInteractor.getUserData()
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                single.subscribe({source.onNext(it.firstName)}) { Log.d("TAG","Беда") }
            }){}
        chatRepository.test(sss)
    }
}