package com.defacto.movieapp.data.remote.util

sealed class UiEvent {
    data class ShowDialog(val code: String?, val message: String?) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    class Action() : UiEvent()
}