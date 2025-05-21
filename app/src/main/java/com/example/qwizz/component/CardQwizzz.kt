package com.example.qwizz.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qwizz.R

@Composable
fun CardQwizzz(
    title: String,
    topic: String,
    duration: String,
    author: String,
    image: Int,
    onClick: () -> Unit
) {
    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )




    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                1.dp,
                color = colorResource(R.color.black_shadow),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.blue_card_main)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.black_shadow),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(2.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = colorResource(R.color.white),
                    letterSpacing = 0.5.sp,
                    modifier = Modifier
                        .weight(0.4f)
                )
                Text(
                    text = topic,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = colorResource(R.color.black),
                    letterSpacing = 2.sp,
                    fontStyle = FontStyle.Italic
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$duration menit",
                        fontFamily = roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Yellow,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(2.dp),

                        ) {
                        Text(
                            text = "by $author",
                            fontFamily = roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = colorResource(R.color.white),
                            letterSpacing = 2.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Right,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}