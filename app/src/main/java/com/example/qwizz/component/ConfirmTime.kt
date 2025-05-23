package com.example.qwizz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qwizz.R

@Preview (showBackground = true)
@Composable
fun ConfirmTime(
    onDismiss: () -> Unit = {},
    onConfirm: (totalDetik: Int) -> Unit = {}
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

    var timeMinute by remember { mutableStateOf("") }
    var timeSecond by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Silahkan Konfirmasi Waktu Pengerjaan",
                fontFamily = pottaOne,
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    shadow = Shadow(
                        color = colorResource(R.color.teal_700),
                        offset = Offset(6f, 6f),
                        blurRadius = 0.1f,
                    )
                ),
                textAlign = TextAlign.Center
            )

        },
        text = {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Menit",
                        fontFamily = p2p,
                        color = colorResource(R.color.teal_200),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            shadow = Shadow(
                                color = colorResource(R.color.white),
                                offset = Offset(1f, 1f),
                                blurRadius = 0.1f,
                            )
                        ),
                        textAlign = TextAlign.Center
                    )

                    BasicTextField(
                        value = timeMinute,
                        onValueChange = {
                            if(it.all { char -> char.isDigit() })
                                timeMinute = it
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .padding(10.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        textStyle = TextStyle(
                            fontFamily = roboto,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        ),
                        decorationBox = {innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ){
                                if (timeMinute.isEmpty()) {
                                    Text(
                                        text = "00",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                innerTextField()
                            }

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )

                }
                Text(
                    text = ":",
                    fontFamily = p2p,
                    color = colorResource(R.color.teal_200),
                    fontSize = 20.sp
                )

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Detik",
                        fontFamily = p2p,
                        color = colorResource(R.color.teal_200),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            shadow = Shadow(
                                color = colorResource(R.color.white),
                                offset = Offset(1f, 1f),
                                blurRadius = 0.1f,
                            )
                        ),
                        textAlign = TextAlign.Center
                    )
                    BasicTextField(
                        value =timeSecond,
                        onValueChange = {
                            if(it.all { char -> char.isDigit() })
                                timeSecond = it
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .padding(10.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        textStyle = TextStyle(
                            fontFamily = roboto,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ){
                                if (timeSecond.isEmpty()) {
                                    Text(
                                        text = "00",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                innerTextField()
                            }

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )

                }



            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val menit = timeMinute.toIntOrNull() ?: 0
                    val detik = timeSecond.toIntOrNull() ?: 0
                    val totalDetik = menit * 60 + detik
                    if(detik in 0..59){
                        onConfirm(totalDetik)
                    }else{
                        onConfirm(0)
                    }
                },
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
        containerColor = Color.Gray
    )

}