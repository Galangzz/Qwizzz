package com.example.qwizz.ui.doqwizz

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun InitialDoQwizzz(
    navController: NavController = rememberNavController(),
    viewModel: DoQwizzzViewModel = viewModel()
){
    val qwizzz by viewModel.currentQwizzz.collectAsState()
//    val qwizzz = remember (qwizzzList) {
//        qwizzzList.firstOrNull() ?: Qwizzz()
//    }
    Log.d("InitialDoQwizzz", "Qwizzz: $qwizzz")
    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)

    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )
    val time = qwizzz?.timeQuiz
    val duration =
        time?.let {
            if (it > 60){
                val minutes = time / 60
                val seconds = time % 60
                "$minutes Menit $seconds Detik"
            }else{
                "$time Detik"
            }
        }
    val scope = rememberCoroutineScope()

    val totalSoal = qwizzz?.question?.size

    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        Log.d("InitialDoQwizzz", "LaunchedEffect called")

    }

    BackHandler(enabled = true) {
        navController.navigate(Screen.searchSelectQwizzz.route){
            popUpTo(0){
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = draw,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TitleComponent()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = backIcon,
                        contentDescription = null,
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                navController.navigate(Screen.searchSelectQwizzz.route){
                                    popUpTo(Screen.searchSelectQwizzz.route){
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.blue_card_main),
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 30.dp,
                        ),
                        shape = RoundedCornerShape(25.dp),
                        border = BorderStroke(1.dp, colorResource(R.color.teal_700))

                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = qwizzz?.title ?: "",
                                fontFamily = roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.white),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Text(
                                text = qwizzz?.topic ?: "",
                                fontFamily = roboto,
                                fontWeight = FontWeight.Normal,
                                color = colorResource(R.color.white),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Text(
                                text = "$totalSoal Soal",
                                fontFamily = roboto,
                                fontWeight = FontWeight.Normal,
                                color = colorResource(R.color.black),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Text(
                                text = duration.toString(),
                                fontFamily = roboto,
                                fontWeight = FontWeight.Normal,
                                color = colorResource(R.color.black),
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Button(
                                onClick = {
                                    if(!isLoading){
                                        scope.launch{
                                            isLoading = true
                                            // go Do Qwizzz
                                            navController.navigate(Screen.mainQwizzz.route){
                                                popUpTo(Screen.searchSelectQwizzz.route){
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                            delay(1000)
                                            isLoading = false
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.teal_700)
                                )
                            ) {
                                if(isLoading){
                                    CircularProgressIndicator(
                                        color = colorResource(R.color.white),
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                }else{
                                    Text(
                                        text = "Mulai",
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.white)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}