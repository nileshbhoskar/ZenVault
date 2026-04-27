package com.nityansh.network.hilt

class ResultWrapper<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    val isSuccess: Boolean
        get() = data != null && error == null

    val isError: Boolean
        get() = error != null
}