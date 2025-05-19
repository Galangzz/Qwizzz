package com.example.qwizz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qwizz.R


@Composable
fun InputTitleQwiz(
    topic: String = "",
    valueText: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    val pottaOne = FontFamily(
        Font(R.font.pottaone, FontWeight.Normal)
    )
    val roboto = FontFamily(
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )
    val p2p = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Judul Qwizzz ${topic}",
                fontFamily = pottaOne,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
                },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()

            ){

                OutlinedTextField(
                    value = valueText,
                    onValueChange = onValueChange,
                    label = {
                        Text(
                            text = "Ketik Judul Qwiz",
                            fontFamily = roboto,
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.White
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White.copy(0.2f),
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Isikan Judul Qwizzz dulu ya sebelum membuat soal ;)",
                    fontFamily = roboto,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontStyle = FontStyle.Italic
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
                ,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                content = {
                    Text(
                        text = "Confirm",
                        fontFamily = p2p,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.DarkGray)
                },
                enabled = valueText.isNotEmpty()
            )

                        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                content = {
                    Text(
                        text = "Dismiss",
                        fontFamily = p2p,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                },
                enabled = true
            )

        },
        modifier = Modifier,
        containerColor = Color.DarkGray,
        shape = RoundedCornerShape(50.dp)
    )


}
@Preview(showBackground = true)
@Composable
fun PreviewAlert(){
    Surface {
        var value by remember { mutableStateOf("") }
//        InputTitleQwiz()
    }
}