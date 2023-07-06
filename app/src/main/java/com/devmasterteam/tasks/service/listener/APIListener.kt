package com.devmasterteam.tasks.service.listener

interface APIListener<T> {

    fun onResolve(result: T)
    fun onReject(message: String)

}