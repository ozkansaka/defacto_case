package com.defacto.movieapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.ui.theme.Red400
import com.defacto.movieapp.ui.theme.Shapes
import com.defacto.movieapp.ui.theme.White
import com.defacto.movieapp.util.ModifierUtil.debouncedClickable
import com.defacto.movieapp.util.ModifierUtil.noRippleDebouncedClickable

@Composable
fun MovieCard(
    image: String?,
    title: String?,
    isFavorite: Boolean,
    publicationDate: String?,
    onClickDetail: () -> Unit,
    onClickFavorite: () -> Unit,
) {
    Column {
        Card(
            modifier = Modifier.debouncedClickable(onClick = onClickDetail),
            shape = Shapes.medium,
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    painter = rememberAsyncImagePainter(
                        model = image
                    ),
                    contentDescription = "image",
                    contentScale = ContentScale.FillBounds,
                )
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopEnd),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = White),
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .noRippleDebouncedClickable(onClick = onClickFavorite)
                    ) {
                        if (isFavorite) {
                            Icon(imageVector = Icons.Outlined.Favorite, tint = Red400, modifier = Modifier.size(24.dp), contentDescription = "")
                        } else {
                            Icon(imageVector = Icons.Outlined.FavoriteBorder, tint = Red400, modifier = Modifier.size(24.dp), contentDescription = "")
                        }
                    }
                }
            }
        }
        Text(
            text = title ?: "",
            maxLines = 2, overflow = TextOverflow.Ellipsis,
            style = PoppinsFontStyle.medium
        )

        Text(
            text = publicationDate ?: "",
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 10.dp),
            style = PoppinsFontStyle.normal
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MovieCardPreview() {
    MovieCard(
        image = "",
        isFavorite = false,
        title = "Avengers",
        publicationDate = "2020",
        onClickDetail = {},
        onClickFavorite = {})
}
