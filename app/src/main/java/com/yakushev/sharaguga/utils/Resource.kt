package com.yakushev.sharaguga.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: Message? = null
) {
    class Success<T>(data: T): Resource<T>(data = data)
    class Error<T>(message: Message?): Resource<T>(message = message)
    class Loading<T>: Resource<T>()
}
