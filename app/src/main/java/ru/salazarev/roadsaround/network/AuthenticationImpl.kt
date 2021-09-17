package ru.salazarev.roadsaround.network

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthenticationImpl @Inject constructor(
    private val fireBaseAuth: FirebaseAuth
) : Authentication {
    override fun registration(email: String, password: String): String {
        val regResult = Tasks.await(fireBaseAuth.createUserWithEmailAndPassword(email, password))
        return regResult.user!!.uid
    }

    override fun authentication(email: String, password: String): String {
        val authResult = Tasks.await(fireBaseAuth.signInWithEmailAndPassword(email, password))
        return authResult.user!!.uid
    }

    override fun resetPassword(email: String): Boolean {
        val result = fireBaseAuth.sendPasswordResetEmail(email)
        return result.isSuccessful
    }

    override fun getUserId(): String = fireBaseAuth.uid!!
}