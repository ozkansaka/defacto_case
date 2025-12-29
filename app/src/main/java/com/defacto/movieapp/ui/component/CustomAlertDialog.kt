package com.defacto.movieapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.defacto.movieapp.ui.theme.Gray100
import com.defacto.movieapp.ui.theme.PoppinsFontStyle
import com.defacto.movieapp.ui.theme.Shapes

@Composable
fun AlertDialog(isOpen: Boolean, title: String?, description: String?, buttonTitle: String? = null, button2Title: String? = null, dismissOnBackPress: Boolean = false, dismissOnClickOutside: Boolean = false, buttonOnClick: () -> Unit, button2OnClick: (() -> Unit)? = null, onDismiss: () -> Unit) {
    if (isOpen) {
        Dialog(properties = DialogProperties(dismissOnBackPress = dismissOnBackPress, dismissOnClickOutside = dismissOnClickOutside), onDismissRequest = { onDismiss() }) {
            Card(
                shape = Shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = Gray100),
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = title ?: "", style = PoppinsFontStyle.normal, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp), textAlign = TextAlign.Center)
                    HorizontalDivider(thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = description ?: "", style = PoppinsFontStyle.normal, fontSize = 15.sp, modifier = Modifier.padding(horizontal = 16.dp), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(32.dp))
                    Column {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Button1(modifier = Modifier.fillMaxWidth(), text = buttonTitle ?: "Tamam", onClick = {
                                buttonOnClick()
                            })
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        button2OnClick?.let {
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Button2(modifier = Modifier.fillMaxWidth(), text = button2Title ?: "İptal", onClick = {
                                    button2OnClick()
                                })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "tr")
@Composable
fun AlertDialogPreview() {
    AlertDialog(isOpen = true, title = "Uyarı", description = "Bu bir uyarıdır", buttonTitle = "Tamam", button2Title = "İptal", buttonOnClick = {}, onDismiss = {})
}