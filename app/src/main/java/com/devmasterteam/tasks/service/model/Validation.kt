package com.devmasterteam.tasks.service.model

class Validation(message: String = "") {
    private var status: Boolean = true
    private var _message = ""

    init {
        if (message.isNotEmpty()){
            _message = message
            status = false
        }
    }

    fun getSatus() = status

    fun getMessage(): String{
        return _message
    }
}