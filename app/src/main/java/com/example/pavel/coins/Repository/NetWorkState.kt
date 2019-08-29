package com.example.pavel.coins.Repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}

class NetWorkState(val status: Status, val msg: String) {

    companion object {

        val LOADED: NetWorkState
        val LOADING: NetWorkState
        val ERROR: NetWorkState

        init {
            LOADED = NetWorkState(Status.SUCCESS, "Успешно загружено")
            LOADING = NetWorkState(Status.RUNNING, "Загружается")
            ERROR = NetWorkState(Status.FAILED, "Что-то не так")
        }
    }

}