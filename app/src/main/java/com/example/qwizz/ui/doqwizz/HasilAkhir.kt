package com.example.qwizz.ui.doqwizz

import android.graphics.drawable.Icon
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
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
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun HasilAkhir(
    navController: NavController = rememberNavController(),
    viewModel: DoQwizzzViewModel = viewModel(),
) {
    val qwizzz by viewModel.currentQwizzz.collectAsState()
    val scores by viewModel.score.collectAsState()

    val roboto = FontFamily(
        Font(R.font.roboto_reguler, FontWeight.Normal),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_ligh, FontWeight.Medium)
    )

    val p2p = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal),
    )
    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)
    val reviewIcon = painterResource(R.drawable.reviewicon)
    val home = painterResource(R.drawable.home)
    val rank = painterResource(R.drawable.rank)

    var benar = (scores / 100.0 * (qwizzz?.question?.size ?: 0)).roundToInt()
    var salah: Int = qwizzz?.question?.size?.minus(benar) ?: 0
    Log.d("HasilAkhir", "score : $scores")
    Log.d("HasilAkhir", "benar: $benar")
    Log.d("HasilAkhir", "salah: $salah")

    BackHandler(enabled = true) {

        Log.d("HasilAkhir", "Back button pressed")
        navController.navigate(Screen.searchSelectQwizzz.route) {
            popUpTo(Screen.searchSelectQwizzz.route) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    LaunchedEffect(Unit) {
        Log.d("HasilAkhir", "LaunchedEffect triggered")
        viewModel.observeCurrentQwizzz()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                                navController.navigate(Screen.searchSelectQwizzz.route) {
                                    popUpTo(Screen.searchSelectQwizzz.route) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                    )
                    Text(
                        text = qwizzz?.title ?: "",
                        fontFamily = roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .weight(1f)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Nilai Anda",
                        fontFamily = roboto,
                        style = TextStyle(
                            shadow = Shadow(
                                color = colorResource(R.color.teal_700),
                                offset = Offset(5.0f, 10.0f),
                                blurRadius = 3f
                            )
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.padding(bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        // TODO: fetch nilai  
                        text = "$scores",
                        fontFamily = p2p,
                        style = TextStyle(
                            shadow = Shadow(
                                color = colorResource(R.color.teal_700),
                                offset = Offset(5.0f, 10.0f),
                                blurRadius = 3f
                            )
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 70.sp,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center

                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    // TODO: Ganti jumbal dijawab
                                    text = "Benar: $benar",
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .weight(0.4f),
                                    textAlign = TextAlign.Start
                                )
                                Row(
                                    modifier = Modifier
                                        .weight(0.6f),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = reviewIcon,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(end = 5.dp),
                                        contentScale = ContentScale.Fit
                                    )

                                    Button(
                                        onClick = {
                                            navController.navigate(Screen.reviewQwizzz.route) {
                                                popUpTo(Screen.searchSelectQwizzz.route) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true

                                            }
                                        },
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .weight(0.2f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorResource(R.color.box3),
                                            contentColor = colorResource(R.color.white)
                                        ),
                                        shape = RectangleShape
                                    ) {

                                        Text(
                                            text = "Review",
                                            fontFamily = roboto,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }


                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    // TODO: Ganti jumbal dijawab
                                    text = "Salah: $salah",
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .weight(0.4f),
                                    textAlign = TextAlign.Start
                                )
                                Row(
                                    modifier = Modifier
                                        .weight(0.6f),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = home,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(end = 5.dp),
                                        contentScale = ContentScale.Fit
                                    )

                                    Button(
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .weight(0.2f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorResource(R.color.box1),
                                            contentColor = colorResource(R.color.white)
                                        ),
                                        shape = RectangleShape
                                    ) {

                                        Text(
                                            text = "Halaman Utama",
                                            fontFamily = roboto,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 50.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rank,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 5.dp),
                                )

                                Button(
                                    onClick = {
                                       navController.navigate(Screen.leaderboard.route) {
                                           popUpTo(Screen.searchSelectQwizzz.route) {
                                               inclusive = false
                                           }
                                           launchSingleTop = true
                                       }
                                    },
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .weight(0.1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.box2),
                                        contentColor = colorResource(R.color.white)
                                    ),
                                    shape = RectangleShape,
                                ) {
                                    Text(
                                        text = "Leaderboard",
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                    }
                    Text(
                        text = "Tetap Semangat, terus belajar ya.....",
                        fontFamily = roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            }
        }
    }
}