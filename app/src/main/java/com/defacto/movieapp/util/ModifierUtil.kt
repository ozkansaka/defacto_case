package com.defacto.movieapp.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ModifierUtil {
    fun Modifier.debouncedClickable(
        debounceDuration: Long = 1000L,
        onClick: () -> Unit
    ): Modifier = composed {
        var isClickable by remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()

        this.clickable(
            enabled = isClickable,
            onClick = {
                if (isClickable) {
                    onClick()
                    isClickable = false
                    scope.launch {
                        delay(debounceDuration)
                        isClickable = true
                    }
                }
            }
        )
    }

    fun Modifier.noRippleDebouncedClickable(
        debounceDuration: Long = 1000L,
        onClick: () -> Unit
    ): Modifier = composed {
        var isClickable by remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()
        val interactionSource = remember { MutableInteractionSource() }


        this.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = isClickable,
            onClick = {
                if (isClickable) {
                    onClick()
                    isClickable = false
                    scope.launch {
                        delay(debounceDuration)
                        isClickable = true
                    }
                }
            }
        )
    }

    fun Modifier.noRippleClickable(
        onClick: () -> Unit
    ): Modifier = composed {
        val interactionSource = remember { MutableInteractionSource() }

        this.clickable(interactionSource = interactionSource,
            indication = null,
            onClick = {
                onClick()
            }
        )
    }
}