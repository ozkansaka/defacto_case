package com.defacto.movieapp.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.defacto.movieapp.R
import com.defacto.movieapp.ui.component.TopBarWithBackButton

@Composable
fun DetailScreen(navController: NavHostController, args: DetailArgs?) {
    DetailContent(
        title = args?.title,
        description = args?.description,
        imageUrl = args?.image,
        onClickBackButton = {
            navController.navigateUp()
        }
    )
}

@Composable
fun DetailContent(title: String?, description: String?, imageUrl: String?, onClickBackButton: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBarWithBackButton(title = stringResource(id = R.string.detail), onClickBackButton = onClickBackButton)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.FillBounds
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title ?: "",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = description ?: "",
                    )
                }
            }
        }

    }
}
