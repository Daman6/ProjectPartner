package com.example.projectpartner.chats

class ChatObject (internal var message: String, var currentUser: Boolean) {
    fun getMessage(): String {
        return message
    }

    fun setMessage(userID: String?) {
        message = message
    }
}