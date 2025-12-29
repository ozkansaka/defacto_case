package com.defacto.movieapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defacto.movieapp.ui.theme.Black
import com.defacto.movieapp.ui.theme.Gray300
import com.defacto.movieapp.ui.theme.Gray50
import com.defacto.movieapp.ui.theme.Gray600
import com.defacto.movieapp.ui.theme.Poppins
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.ui.theme.Red400
import com.defacto.movieapp.ui.theme.Shapes
import com.defacto.movieapp.util.ModifierUtil.noRippleDebouncedClickable

@Composable
fun CustomTextField(
    label: String? = null,
    hint: String? = null,
    value: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Column {
        label?.let {
            Text(text = label, fontSize = 14.sp, style = PoppinsFontStyle.normal)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Card(
            shape = Shapes.extraSmall,
            colors = CardDefaults.cardColors(containerColor = if (enabled) Gray50 else Gray300),
            border = BorderStroke(1.dp, if (isError) Red400 else if (isFocused) Gray600 else Gray300)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    color = Black
                ),
                enabled = enabled,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(8.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = hint ?: "",
                                    fontSize = 15.sp,
                                    style = PoppinsFontStyle.normal,
                                    color = Gray600
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }
        if (isError && errorMessage?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .width(14.67.dp)
                        .height(12.67.dp),
                    imageVector = Icons.Filled.Warning, contentDescription = "", tint = Red400
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = errorMessage, fontSize = 12.sp, style = PoppinsFontStyle.normal, color = Red400)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        value = "",
        hint = "Adınızı girin",
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = {}
    )
}

@Composable
fun PasswordTextField(
    label: String? = null,
    hint: String? = null,
    value: String,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column {
        label?.let {
            Text(text = label, fontSize = 14.sp, style = PoppinsFontStyle.normal)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Card(
            shape = Shapes.extraSmall,
            colors = CardDefaults.cardColors(containerColor = if (enabled) Gray50 else Gray300),
            border = BorderStroke(1.dp, if (isError) Red400 else if (isFocused) Gray600 else Gray300)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    color = Black
                ),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(8.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = hint ?: "",
                                    fontSize = 15.sp,
                                    style = PoppinsFontStyle.normal,
                                    color = Gray600
                                )
                            }
                            innerTextField()
                        }

                        Box(modifier = Modifier.noRippleDebouncedClickable {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
        if (isError && errorMessage?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .width(14.67.dp)
                        .height(12.67.dp),
                    imageVector = Icons.Filled.Warning, contentDescription = "", tint = Red400
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = errorMessage, fontSize = 12.sp, style = PoppinsFontStyle.normal, color = Red400)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(
        value = "",
        hint = "Adınızı girin",
        onValueChange = {}
    )
}


class ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val parts = originalText.split(',')

        val integerPart = parts.getOrNull(0)?.let { formatIntegerPart(it) } ?: ""
        val decimalPart = parts.getOrNull(1) ?: ""

        val formattedText = if (decimalPart.isNotEmpty()) {
            "$integerPart,$decimalPart"
        } else if (originalText.contains(",")) {
            "$integerPart,"
        } else {
            integerPart
        }

        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = offset
                if (offset > 3) {
                    transformedOffset += (offset - 1) / 3
                }
                return transformedOffset.coerceAtMost(formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = offset
                if (offset > 3) {
                    originalOffset -= (offset - 1) / 4
                }
                return originalOffset.coerceAtMost(originalText.length)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMap)
    }

    private fun formatIntegerPart(text: String): String {
        val reversed = text.reversed()
        val grouped = reversed.chunked(3).joinToString(".")
        return grouped.reversed()
    }
}