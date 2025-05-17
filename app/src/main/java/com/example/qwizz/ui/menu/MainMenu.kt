package com.example.qwizz.ui.menu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainMenu(
    navController: NavController,
){
    val auth = FirebaseAuth.getInstance()
    val authUser = auth.currentUser
    val pressStart2P = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )

    val draw = painterResource(R.drawable.bg)
    Log.d("MainMenu", "User email: ${authUser?.uid}")

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = draw,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Qwizzz.",
                    fontSize = 24.sp,
                    fontFamily = pressStart2P,
                    modifier = Modifier,
                    color = colorResource(R.color.blue)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                // Kerjakan Qwizz
                Card (
                    modifier = Modifier
                        .width(210.dp)
                        .height(210.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = colorResource(R.color.blue_card_main)
                    ),
                    onClick = {
                        //Menuju ke Halaman Pilih Qwizz
                    }

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painterResource(R.drawable.kerjakan_qwizzz_img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,

                            modifier = Modifier
                                .padding(10.dp)
                                .size(120.dp)
                        )
                        Text(
                            text = "Kerjakan Qwizzz",
                            fontFamily = pressStart2P,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }


                //Buat Qwizzz
                Card (
                    modifier = Modifier
                        .width(210.dp)
                        .height(210.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = colorResource(R.color.blue_card_main)
                    ),
                    onClick = {
                        navController.navigate(Screen.SelectTopic.route)
                    }

                ){
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Image(
                            painterResource(R.drawable.buat_qwizzz_icon),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,

                            modifier = Modifier
                                .padding(10.dp)
                                .size(120.dp)
                        )
                        Text(
                            text = "Buat Qwizzz.",
                            fontFamily = pressStart2P,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                //Info
                Card (
                    modifier = Modifier
                        .width(210.dp)
                        .height(210.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = colorResource(R.color.blue_card_main)
                    ),
                    onClick = {
                        navController.navigate(Screen.StatsMenu.route)
                    }

                ){
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Image(
                            painterResource(R.drawable.stats_account_icon),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,

                            modifier = Modifier
                                .padding(10.dp)
                                .size(120.dp)
                        )
                        Text(
                            text = "Stats",
                            fontFamily = pressStart2P,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

            }

        }

    }
}