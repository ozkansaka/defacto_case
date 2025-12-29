package com.defacto.movieapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defacto.movieapp.ui.theme.Gray100
import com.defacto.movieapp.ui.theme.Gray300
import com.defacto.movieapp.ui.theme.Gray600
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.ui.theme.Shapes

@Composable
fun SearchBar(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = Shapes.medium,
    ) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            textStyle = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Gray600
            ),
            modifier = modifier
                .fillMaxWidth()
                .background(Gray100)
                .height(48.dp)
                .padding(12.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(1.dp), imageVector = Icons.Outlined.Search, contentDescription = "", tint = Gray100
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(text = hint, fontSize = 17.sp, style = PoppinsFontStyle.normal, color = Gray300)
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar(value = "", hint = "Ara", onValueChange = {})
}