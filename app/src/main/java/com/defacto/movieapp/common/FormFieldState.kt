package com.defacto.movieapp.common

data class FormFieldState(
    var value: String = "",
    var index: Int = 0,
    var error: Boolean? = null,
    var errorMessage: String = ""
)