package com.example.qwizz.ui.doqwizz

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel

@Preview(showBackground = true)
@Composable
fun ReviewQwizzz(
    navController: NavController = rememberNavController(),
    qwizzzVM: DoQwizzzViewModel = viewModel()
) {
    val qwizzzList by qwizzzVM.quizList.collectAsState()
    val stackAnswer by qwizzzVM.stackAnswer.collectAsState()
    val qwizzz = remember(qwizzzList) {
        qwizzzList.firstOrNull() ?: Qwizzz()
    }


    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)
    val roboto = FontFamily(
        Font(R.font.roboto_reguler, FontWeight.Normal),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_ligh, FontWeight.Medium)
    )

    BackHandler (enabled = true){
        navController.navigate(Screen.searchSelectQwizzz.route) {
            popUpTo(Screen.searchSelectQwizzz.route) {
                inclusive = false
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
                                navController.navigate(Screen.hasilAkhir.route) {
                                    popUpTo(Screen.searchSelectQwizzz.route) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                    )
                    Text(
                        // TODO: Judul Qwizzz
                        text = qwizzz.title,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .verticalScroll(remember { ScrollState(0) })
                            .weight(1f)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                //Card Qwizz
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        itemsIndexed(qwizzz.question) { index, question ->
                            val userAnswer = stackAnswer.getOrNull(index)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(R.color.blue_box),
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 20.dp,
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 10.dp, horizontal = 15.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
                                            .background(
                                                color = colorResource(R.color.teal_200),
                                                shape = RectangleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            // TODO: Pertanyaan
                                            text = "${index + 1}. ${question.questionText}",
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxSize(),
                                            fontFamily = roboto,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                        )
                                    }

                                    question.options.forEach { option ->
                                        val isSelected = option.text == userAnswer?.text
                                        val isCorrect = option.correct

                                        val backgroundColor = when {
                                            isCorrect && isSelected -> Color.Green
                                            isSelected && !isCorrect -> Color.Red
                                            isCorrect && !isSelected -> Color.Green
                                            else -> Color.DarkGray.copy(0.7f)
                                        }

                                        // Answer 1
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = backgroundColor
                                            ),
                                            onClick = {},
                                            enabled = true
                                        ) {
                                            Text(
                                                text = option.text,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .fillMaxSize(),
                                                fontFamily = roboto,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }


                                    }
                                    if (userAnswer?.text.isNullOrEmpty()) {
                                        Text("❌ Jawaban tidak diisi", color = Color.Gray)
                                    }

                                }

                            }


                        }
                    }
                }
            }
        }
    }
}