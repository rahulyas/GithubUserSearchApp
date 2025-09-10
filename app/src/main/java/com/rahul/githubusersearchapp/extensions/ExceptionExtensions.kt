package com.rahul.githubusersearchapp.extensions

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Exception.getUserFriendlyMessage(): String {
    return when (this) {
        is SocketTimeoutException -> "Connection timeout. Please check your internet connection."
        is IOException -> "Network error. Please check your internet connection."
        is HttpException -> {
            when (this.code()) {
                404 -> "User not found. Please check the username and try again."
                403 -> "Rate limit exceeded. Please try again later."
                else -> "Server error occurred. Please try again later."
            }
        }
        else -> "An unexpected error occurred. Please try again."
    }
}