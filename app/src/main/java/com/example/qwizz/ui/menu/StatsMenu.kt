package com.example.qwizz.ui.menu

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.LineChartComp
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.viewmodel.auth.AuthViewModel
import com.example.qwizz.viewmodel.auth.AuthViewModelFactory
import com.example.qwizz.viewmodel.stats.StatsViewModel
import com.google.firebase.auth.FirebaseAuth

@Preview(showBackground = true)
@Composable
fun StatsMenu(
    navController: NavController = rememberNavController(),
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val userStatsViewModel: StatsViewModel = viewModel()

    val userStats by userStatsViewModel.userStats.collectAsState()

    var isLoading by remember { mutableStateOf(false) }

    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)

    val pressStart2P = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )
    val roboto = FontFamily(
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )

    val mathScore = userStats?.mathscore
    val listMathScore: List<Int> = mathScore ?: emptyList()
    val bahasaScore = userStats?.bahasascore
    val listBahasaScore: List<Int> = bahasaScore ?: emptyList()


    val totalSoalDikerjakan: Int = 0
    val rataRataNilai: Int = 0
    val jawabanBenar: Int = 0

    val user = FirebaseAuth.getInstance().currentUser
    Log.d("StatsMenu", "User email: ${user?.email}")

    BackHandler(enabled = true) {
        navController.navigate(Screen.mainMenu.route) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    LaunchedEffect(Unit) {
        Log.d("StatsMenu", "LaunchedEffect called")
        userStatsViewModel.getDataUser(user?.uid ?: "")
        Log.d("StatsMenu", "getDataUser called")
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorResource(R.color.blue_head_up)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        TitleComponent()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
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
                                        navController.navigate(Screen.mainMenu.route) {
                                            popUpTo(0) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(80.dp)
                            )
                            Text(
                                text = "Stats",
                                fontFamily = pressStart2P,
                                fontSize = 18.sp,
                                color = colorResource(R.color.blue_rect),
                                modifier = Modifier.padding(start = 10.dp)
                            )

                        }

                    }


                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp, max = 550.dp)
                            .padding(horizontal = 10.dp)
                            .verticalScroll(rememberScrollState(0)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        Text(
                            text = "Halooo",
                            fontFamily = pressStart2P,
                            color = colorResource(R.color.white),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            style = TextStyle(
                                letterSpacing = 2.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Start,
                                shadow = Shadow(
                                    color = colorResource(R.color.blue),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Text(
                            text = userStats?.name.toString(),
                            fontFamily = pressStart2P,
                            color = colorResource(R.color.white),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                letterSpacing = 2.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Start,
                                shadow = Shadow(
                                    color = colorResource(R.color.blue),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        //Card Stats
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.blue_rect),
                                contentColor = colorResource(R.color.white)
                            )

                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {

                                //ke db ambil nilai

                                Text(
                                    text = "Jumlah soal dikerjakan: ${totalSoalDikerjakan}",
                                    fontFamily = roboto,
                                    color = colorResource(R.color.white),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Text(
                                    text = "Rata-rata nilai: ${rataRataNilai}",
                                    fontFamily = roboto,
                                    color = colorResource(R.color.white),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Text(
                                    text = "Jawaban Benar: ${jawabanBenar}",
                                    fontFamily = roboto,
                                    color = colorResource(R.color.white),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                        }
                        if (listMathScore.isNotEmpty()){
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(R.color.blue_box),
                                    contentColor = colorResource(R.color.white)
                                )
                            ){
                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = "Grafik Nilai Matematika",
                                        fontFamily = roboto,
                                        color = colorResource(R.color.white),
                                        fontSize = 16.sp,

                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    LineChartComp(listMathScore)
                                }

                            }
                        }
                        if (listBahasaScore.isNotEmpty()){
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp, vertical = 15.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(R.color.blue_box),
                                    contentColor = colorResource(R.color.white)
                                )
                            ){
                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = "Grafik Nilai Bahasa",
                                        fontFamily = roboto,
                                        color = colorResource(R.color.white),
                                        fontSize = 16.sp,

                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    LineChartComp(listBahasaScore)
                                }

                            }
                        }








                    }
                    Button(
                        onClick = {
                            isLoading = true
                            authViewModel.logoutUser() { success, message ->
                                if (success) {
                                    Log.d("StatsMenu", "Logout success")
                                    isLoading = false
                                } else {
                                    Log.d("StatsMenu", "Logout failed: $message")
                                    isLoading = false
                                    return@logoutUser
                                }
                            }
                            navController.navigate(Screen.loginScreen.route) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                                launchSingleTop = true

                            }
                            Log.d("StatsMenu", "Logout button clicked")

                        },
                        modifier = Modifier
                            .width(100.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = colorResource(R.color.white),
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(text = "Logout")
                        }
                    }


                }

            }

        }
    }
}
