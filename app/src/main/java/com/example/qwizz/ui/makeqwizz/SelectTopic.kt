package com.example.qwizz.ui.makeqwizz

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.TitleComponent


//@Preview(showBackground = true)
@Composable
fun SelectTopic(
    navController: NavController
){
    val presstart2P = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )

    val mathIcon = painterResource(R.drawable.math_selecqwizz_icon)
    val bahasaIcon = painterResource(R.drawable.bahasa_select_qwizz_icon)
    val draw = painterResource(R.drawable.bg)
    val back_icon = painterResource(R.drawable.back_icon)

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ){
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = draw,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TitleComponent()
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = back_icon,
                        contentDescription = null,
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable{
                                navController.navigate(Screen.mainMenu.route)
                            }
                    )
                }
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    Row(
                        modifier = Modifier
                            .width(320.dp)
                            .padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){

                        Text(
                            text = "Silahkan Pilih Qwizzz Topik",
                            fontFamily = presstart2P,
                            color = colorResource(R.color.white),
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Normal,
                                shadow = Shadow(
                                    color = colorResource(R.color.blue_rect),
                                    offset = Offset(12f, 12f),
                                    blurRadius = 0.1f,

                                )

                            )

                        )
                    }
                    Card(
                        modifier = Modifier
                            .size(180.dp)
                            .clickable{
                                /*Set Topic*/
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.blue_box),
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 20.dp,
                            pressedElevation = 22.dp,
                            focusedElevation = 21.dp,
                            hoveredElevation = 21.dp
                        )

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = mathIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(80.dp)

                            )
                            Text(
                                text = "Matematika",
                                fontFamily = presstart2P,
                                color = colorResource(R.color.white),
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center,

                            )
                        }
                    }


                    Card(
                        modifier = Modifier
                            .size(180.dp)
                            .clickable{
                                /*Set Topic*/
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.blue_box),
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 20.dp,
                            pressedElevation = 22.dp,
                            focusedElevation = 21.dp,
                            hoveredElevation = 21.dp
                        )

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = bahasaIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(80.dp)

                            )
                            Text(
                                text = "Bahasa",
                                fontFamily = presstart2P,
                                color = colorResource(R.color.white),
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center,

                                )
                        }
                    }




                }
            }


        }
    }

}