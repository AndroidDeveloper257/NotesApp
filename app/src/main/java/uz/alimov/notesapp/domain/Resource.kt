package uz.alimov.notesapp.domain

sealed class Resource<out S: Any, out E: Any> {
    data class Success<S: Any>(val data: S): Resource<S, Nothing>()
    data class Error<E: Any>(val rawResponse: E): Resource<Nothing, E>()
}