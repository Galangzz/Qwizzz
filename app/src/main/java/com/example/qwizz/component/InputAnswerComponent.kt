package com.example.qwizz.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qwizz.R


@Composable
fun InputAnswerComponent(
    value: String,
    onValueChange: (String) -> Unit,
    isCorrect: Boolean,
    onIsCorrectChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    colorBox: Int = R.color.box1,
    context: Context,
    countCheck: Int = 0
){

    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                colorResource(colorBox),
                shape = RoundedCornerShape(18.dp)
            ),
        textStyle = TextStyle(
            color = colorResource(R.color.black),
            fontFamily = roboto,
            fontWeight = FontWeight.ExtraLight,
            fontSize = 16.sp
        ),
        decorationBox = { innerTextField ->

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ){
                    if (value.isEmpty()) {
                        Text(
                            text = "Ketik Jawaban",
                            color = colorResource(R.color.black),
                            fontFamily = roboto,
                            fontWeight = FontWeight.ExtraLight
                        )
                    }
                    innerTextField()

                }
                Checkbox(
                    checked = isCorrect,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            if (value.isEmpty()) {
                                Toast.makeText(context, "Jawaban tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                return@Checkbox
                            }
                            if (countCheck >= 1) {
                                Toast.makeText(context, "Hanya bisa memilih 1 jawaban yang benar", Toast.LENGTH_SHORT).show()
                                return@Checkbox
                            }
                        }
                        onIsCorrectChange(isChecked)
                    },
                    enabled = enabled
                )

            }
        },
    )
}