package com.mobile.drive.mobile.utils

data class Resource<out T>(val status: Status, val data: T?, val error: ErrorData?) {
    companion object {
        fun <T> success(data: T? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(error: ErrorData, data: T? = null): Resource<T> {
            return Resource(Status.FAILED, data, error)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.RUNNING, data, null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.EMPTY, null, null)
        }
    }
}
