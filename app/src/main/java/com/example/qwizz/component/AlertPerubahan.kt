package com.example.qwizz.component

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qwizz.R

@Composable
fun AlertPerubahan(
    title: String = "",
    text: String = "",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
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
                text = title,
                fontFamily = pottaOne,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        },
        text = {
            Text(
                text = text,
                fontFamily = roboto,
                fontWeight = FontWeight.Normal,
                color = Color.Yellow
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                content = {
                    Text(
                        text = "Ya",
                        fontFamily = p2p,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                },
                enabled = true

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
                        text = "Batal",
                        fontFamily = p2p,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.White
                    )

                },
                enabled = true
            )
        },
        containerColor = Color.DarkGray,
    )
}

@Preview (showBackground = true)
@Composable
fun PreviewAlertPerubahan(){
    AlertPerubahan()

}