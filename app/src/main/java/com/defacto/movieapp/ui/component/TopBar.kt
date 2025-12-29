package com.defacto.movieapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Dry
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.util.ModifierUtil.noRippleDebouncedClickable

@Composable
fun TopBar(title: String = "", containerColor: Color = MaterialTheme.colorScheme.background) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .align(Alignment.Center), contentAlignment = Alignment.Center
            ) {
                Text(
                    title,
                    fontSize = when {
                        title.length <= 15 -> 22.sp
                        title.length <= 20 -> 20.sp
                        else -> 18.sp
                    },
                    style = PoppinsFontStyle.medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar("")
}


@Composable
fun TopBarWithBackButton(title: String = "", containerColor: Color = MaterialTheme.colorScheme.background, onClickBackButton: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .noRippleDebouncedClickable { onClickBackButton() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = ""
                )
            }
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .align(Alignment.Center), contentAlignment = Alignment.Center
            ) {
                Text(
                    title,
                    fontSize = when {
                        title.length <= 15 -> 22.sp
                        title.length <= 20 -> 20.sp
                        else -> 18.sp
                    },
                    style = PoppinsFontStyle.medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarWithBackButtonPreview() {
    TopBarWithBackButton("", onClickBackButton = {})
}

