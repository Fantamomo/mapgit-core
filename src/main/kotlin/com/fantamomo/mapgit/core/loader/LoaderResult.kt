package com.fantamomo.mapgit.core.loader

sealed interface LoaderResult<T> {

    data class Success<T>(val value: T) : LoaderResult<T>

    data class Failure(val exception: Exception) : LoaderResult<Nothing>

    data object NotFound : LoaderResult<Nothing>

    data object Empty : LoaderResult<Nothing>

    data object TypeMismatch : LoaderResult<Nothing>

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <T> success(value: T) = Success(value)
        fun <T> failure(exception: Exception): LoaderResult<T> = Failure(exception) as LoaderResult<T>
        fun <T> notFound() = NotFound as LoaderResult<T>
        fun <T> empty() = Empty as LoaderResult<T>
        fun <T> typeMismatch() = TypeMismatch as LoaderResult<T>
    }
}