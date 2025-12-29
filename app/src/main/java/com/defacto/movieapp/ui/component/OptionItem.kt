package com.defacto.movieapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.util.ModifierUtil.noRippleDebouncedClickable

@Composable
fun OptionItem(title: String, contentDescription: String = "", onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .noRippleDebouncedClickable { onClick() }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(text = title, fontSize = 15.sp, style = PoppinsFontStyle.normal)
                }
            }
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = contentDescription)
        }
        HorizontalDivider(thickness = 0.5.dp)
    }
}

@Preview(showBackground = true, locale = "tr")
@Composable
fun OptionItemPreview() {
    OptionItem(title = "Seçenek 1", contentDescription = "", onClick = {})
}