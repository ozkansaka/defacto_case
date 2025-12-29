package com.defacto.movieapp.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.defacto.movieapp.R

val Poppins = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val PoppinsFontStyle = CustomTypography(
    light = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Light,
    ),
    normal = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
    ),
    medium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
    ),
    bold = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
    )
)

data class CustomTypography(
    val light: TextStyle,
    val normal: TextStyle,
    val medium: TextStyle,
    val bold: TextStyle
)