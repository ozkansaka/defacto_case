package com.defacto.movieapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.defacto.movieapp.ui.theme.Blue900
import com.defacto.movieapp.ui.theme.TransparentGray


@Composable
fun LoadingBar(isVisibility: Boolean, color: Color = TransparentGray) {
    if (isVisibility) {
        Surface(
            modifier = Modifier
                .fillMaxSize(), color = color
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = Blue900)
            }
        }
    }
}

@Preview
@Composable
fun LoadingBarPreview() {
    LoadingBar(isVisibility = true)
}