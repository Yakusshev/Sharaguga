package com.yakushev.data

sealed class Resource<T>(
    val data: T? = null,
    val message: Message? = null
) {

    class Success<T>(
        data: T,
        val change: Change = Change.Get
    ): Resource<T>(data = data)

    class Error<T>(message: Message?): Resource<T>(message = message)

    class Loading<T>: Resource<T>()

}

sealed class Change(
    val index: Int,
    var observed: Boolean = false
) {
    object Get : Change(-1, true)

    class Added(
        index: Int
    ) : Change(index = index)

    class Modified(
        index: Int
    ) : Change(index = index)

    class Removed(
        index: Int
    ) : Change(index = index)
}
