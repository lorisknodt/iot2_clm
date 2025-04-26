package com.example.clm_iot2.singleton

import com.example.clm_iot2.model.User

object CurrentUser {
    private var user: User? = null
    private var clientId: String? = null

    fun setUser(loggedInUser: User) {
        user = loggedInUser
        clientId = loggedInUser.id
    }

    fun getUser(): User? {
        return user
    }

    fun getClientId(): String? {
        return clientId
    }

    fun login(id: String) {
        clientId = id
    }

    fun logout() {
        user = null
        clientId = null
    }

    fun isLoggedIn(): Boolean {
        return clientId != null
    }
}
