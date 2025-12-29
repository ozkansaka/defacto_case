package com.defacto.movieapp.common

data class AlertDialogState(
    var isOpen: Boolean = false,
    var title: String = "",
    var description: String = "",
    var buttonTitle: String? = null,
    var button2Title: String? = null,
    var referenceType: String? = null,
)