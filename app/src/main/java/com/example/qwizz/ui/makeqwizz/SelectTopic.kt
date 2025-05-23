package com.example.qwizz.ui.makeqwizz

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.InputTitleQwiz
import com.example.qwizz.component.TitleComponent


//@Preview(showBackground = true)
@Composable
fun SelectTopic(
    navController: NavController
){
    val presstart2P = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )

    val pottaOne = FontFamily(
        Font(R.font.pottaone, FontWeight.Normal)
    )
    var topic by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val mathIcon = painterResource(R.drawable.math_selecqwizz_icon)
    val bahasaIcon = painterResource(R.drawable.bahasa_select_qwizz_icon)
    val draw = painterResource(R.drawable.bg)
    val backicon = painterResource(R.drawable.back_icon)

    val context = LocalContext.current

    if (showDialog) {
        InputTitleQwiz(
            topic = topic,
            valueText = title,
            onValueChange = { title = it },
            onDismiss = {
                showDialog = false
                Log.d("SelectTopic", "Dismiss")
                title = ""

                        },
            onConfirm = {
                if (title.isNotEmpty() && topic.isNotEmpty()){
                    if (title.length < 3 || title.length > 32){
                        Toast.makeText(context, "Judul Qwiz harus antara 3-32 karakter", Toast.LENGTH_SHORT).show()
                        title = ""
                        return@InputTitleQwiz
                    }
                    Log.d("SelectTopic", "Title: $title, Topic: $topic")
                navController.navigate(Screen.inputQuestion.withArgs(topic, title)){
                    popUpTo(0){
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                    showDialog = false

                }else{
                    Toast.makeText(context, "Judul Qwiz tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    title = ""
                    return@InputTitleQwiz
                }

                        }
        )
    }

    BackHandler(enabled = true) {
        navController.navigate(Screen.mainMenu.route){
            popUpTo(0){
                inclusive = true
            }
            launchSingleTop = true
        }
    }

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
                        painter = backicon,
                        contentDescription = null,
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable{
                                navController.navigate(Screen.mainMenu.route){
                                    popUpTo(0){
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
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
                            fontFamily = pottaOne,
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
                                topic = "Matematika"
                                showDialog = true
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
                                topic = "Bahasa"
                                showDialog = true
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