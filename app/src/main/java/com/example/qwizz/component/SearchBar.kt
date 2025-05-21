package com.example.qwizz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qwizz.R

@Composable
fun SearchBar(
    value : String,
    onValueChange : (String) -> Unit,
    onClick : () -> Unit
){
    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.blue_box)
        ),
        shape = RoundedCornerShape(100.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.blue_head_up)
            ),
            shape = RoundedCornerShape(100.dp)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp),
                shape = RoundedCornerShape(30.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.white),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .height(50.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(
                            color = colorResource(R.color.black),
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 4.sp
                        ),
                        decorationBox = { innerTextField ->
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ){
                                Box(
                                    modifier = Modifier
                                        .weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ){
                                    if (value.isEmpty()) {
                                        Text(
                                            text = "Search Qwizzz",
                                            color = Color.DarkGray.copy(0.7f),
                                            fontFamily = roboto,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                    innerTextField()

                                }
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = colorResource(R.color.black),
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable (
                                            enabled = value.isNotEmpty(),
                                            onClick = onClick
                                        ),

                                )

                            }
                        },
                        singleLine = true
                    )
                }
            }

        }

    }
}