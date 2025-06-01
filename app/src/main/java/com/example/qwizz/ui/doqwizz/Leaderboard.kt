package com.example.qwizz.ui.doqwizz

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.example.qwizz.data.model.LeaderboardUser
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel

@Preview(showBackground = true)
@Composable
fun Leaderboard(
    navController: NavController = rememberNavController(),
    qwizzzVM: DoQwizzzViewModel = viewModel()
) {
    val leaderboardList by qwizzzVM.leaderboard.collectAsState()

    val quiz by qwizzzVM.currentQwizzz.collectAsState()


    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)
    val roboto = FontFamily(
        Font(R.font.roboto_reguler, FontWeight.Normal),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_ligh, FontWeight.Medium)
    )
    LaunchedEffect(Unit) {
        Log.d(TAG, "LaunchedEffect triggered")
        qwizzzVM.observeCurrentQwizzz()
        Log.d(TAG, "Leaderboard: $leaderboardList")
    }

    Surface {
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
                                navController.navigate(Screen.hasilAkhir.route){
                                    popUpTo(Screen.searchSelectQwizzz.route){
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                    )
                    Text(
                        // TODO: Judul Qwizzz
                        text = quiz?.title ?: "",
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        shape = RectangleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp,
                        )
                    ) {
                        Text(
                            text = "Leaderboard",
                            fontFamily = roboto,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                        ) {
                            itemsIndexed(leaderboardList) { index, leaderboard ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "${index + 1}. ${leaderboard.username}",
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .padding(5.dp)
                                    )
                                    Text(
                                        text = "${leaderboard.score}",
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .weight(0.2f)
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