package com.defacto.movieapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defacto.movieapp.ui.theme.Black
import com.defacto.movieapp.ui.theme.Blue900
import com.defacto.movieapp.ui.theme.Gray100
import com.defacto.movieapp.ui.theme.Gray300
import com.defacto.movieapp.ui.theme.Gray600
import com.defacto.movieapp.ui.theme.Pink80
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.ui.theme.Shapes
import com.defacto.movieapp.ui.theme.White
import com.defacto.movieapp.util.ModifierUtil.debouncedClickable


@Composable
fun Button1(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        shape = Shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Blue900 else Gray300
        ),
        modifier = modifier
            .height(40.dp)
            .wrapContentWidth()
            .debouncedClickable { if (enabled) onClick() }
    ) {
        Box(
            modifier = modifier.fillMaxHeight()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                style = PoppinsFontStyle.medium,
                color = White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Button1Preview() {
    Button1(text = "Login") {}
}

@Composable
fun Button2(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        shape = Shapes.extraSmall,
        border = BorderStroke(1.dp, Gray600),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        modifier = modifier
            .height(40.dp)
            .wrapContentWidth()
            .debouncedClickable { if (enabled) onClick() }
    ) {
        Box(
            modifier = modifier.fillMaxHeight()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                style = PoppinsFontStyle.medium,
                color = Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Button2Preview() {
    Button2(text = "Login") {}
}