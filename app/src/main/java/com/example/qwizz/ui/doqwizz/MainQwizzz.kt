package com.example.qwizz.ui.doqwizz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.example.qwizz.component.AlertPerubahan
import com.example.qwizz.component.CountDownTimer
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel

private const val TAG = "MainQwizzz"

@Composable
fun MainQwizzz(
    navController: NavController = rememberNavController(),
    qwizzzVM: DoQwizzzViewModel = viewModel()
){

    val qwizzzList by qwizzzVM.quizList.collectAsState()
    val qwizzz = remember (qwizzzList) {
        qwizzzList.firstOrNull() ?: Qwizzz()
    }
    val scores by qwizzzVM.score.collectAsState()

    if (qwizzz.question.isEmpty()){
        navController.navigate(Screen.mainMenu.route)
    }else{
        Log.d(TAG, "MainQwizzz: $qwizzz")
        Log.d(TAG, "MainQwizzz: ${qwizzz.timeQuiz}")
    }

    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)

    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )
    val title = qwizzz.title.toString()

    val questionSize = qwizzz.question.size
    var currentQuestionIndex by remember { mutableIntStateOf(0) }

    var currentQuestionText by remember { mutableStateOf("") }
    var answerOption1 by remember { mutableStateOf("") }
    var answerOption2 by remember { mutableStateOf("") }
    var answerOption3 by remember { mutableStateOf("") }
    var answerOption4 by remember { mutableStateOf("") }

    var score by remember { mutableDoubleStateOf(0.0) }


    var stackAnswer = remember { mutableStateListOf<AnswerOption>() }

    var showDialogBack by remember { mutableStateOf(false) }
    var showDialogSubmit by remember { mutableStateOf(false) }

    if (showDialogBack){
        AlertPerubahan(
            title = "Peringatan",
            text = "Apakah anda yakin ingin keluar?",
            onDismiss = {
                showDialogBack = false
            },
            onConfirm = {
                showDialogBack = false
                navController.navigate(Screen.mainMenu.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        )
    }

    if (showDialogSubmit){
        AlertPerubahan(
            title = "Peringatan",
            text = "Soal belum selesai, apakah anda yakin ingin submit?",
            onDismiss = {
                showDialogSubmit = false
            },
            onConfirm = {
                showDialogSubmit = false
                qwizzzVM.countScore(stackAnswer)
                qwizzzVM.addScore()
                navController.navigate(Screen.hasilAkhir.route){
                    popUpTo(Screen.searchSelectQwizzz.route){
                        inclusive = false
                    }
                    launchSingleTop = true
                }

            }
        )
    }

    LaunchedEffect(currentQuestionIndex) {
        Log.d(TAG, "LaunchedEffect triggered")
        Log.d(TAG, "Current Question Index: $currentQuestionIndex")
        Log.d(TAG, "Question Size: $questionSize")
        val currentQ = qwizzz.question[currentQuestionIndex]
        currentQuestionText = currentQ.questionText
        answerOption1 = currentQ.options[0].text
        answerOption2 = currentQ.options[1].text
        answerOption3 = currentQ.options[2].text
        answerOption4 = currentQ.options[3].text

        while (stackAnswer.size < questionSize) {
            stackAnswer.add(AnswerOption("", false))
        }
        Log.d(TAG, "Qwizz Size: ${qwizzz.question.size}")
        Log.d(TAG, "Stack size: ${stackAnswer.size}")
        Log.d(TAG, "Current Question: $currentQuestionText")
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = backIcon,
                        contentDescription = null,
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                showDialogBack = true
                            }
                    )
                    Text(
                        text = title,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .weight(1f),
                        fontFamily = roboto,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White,
                            shadow = Shadow(
                                color = Color.Blue,
                                blurRadius = 10f,
                                offset = Offset(2f, 2f)
                            )
                        ),
                    )
                }

                //Card Qwizz
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(remember { ScrollState(0) })
                        .padding(horizontal = 20.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    //timer
                    CountDownTimer(totalTime = qwizzz.timeQuiz){
                        Log.d(TAG, "Timer finished")
                        val score = qwizzzVM.countScore(stackAnswer)
                        qwizzzVM.addScore()
                        navController.navigate(Screen.hasilAkhir.route){
                            popUpTo(Screen.searchSelectQwizzz.route){
                                inclusive = false
                            }
                            launchSingleTop = true

                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.blue_box),
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 20.dp,
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
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
                                    text = currentQuestionText,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxSize(),
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    )
                            }

                            // Answer 1
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if ( stackAnswer.getOrNull(currentQuestionIndex)?.text == answerOption1) colorResource(R.color.box1).copy(0.5f) else colorResource(R.color.box1),
                                ),
                                onClick = {
                                    Log.d(TAG, "Answer 1 Clicked")
                                    val addUpdate = AnswerOption(answerOption1, true)

                                    stackAnswer[currentQuestionIndex] = addUpdate
                                    Log.d(TAG, "Stack : ${stackAnswer.size}")
                                    Log.d(TAG, "Stack : $stackAnswer")
                                    Log.d(TAG, "Stack : ${stackAnswer[currentQuestionIndex]}")

                                },
                                enabled = true
                            ){
                                Text(
                                    text = answerOption1,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxSize(),
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Answer 2
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if ( stackAnswer.getOrNull(currentQuestionIndex)?.text == answerOption2) colorResource(R.color.box2).copy(0.5f) else colorResource(R.color.box2),
                                ),
                                onClick = {
                                    Log.d(TAG, "Answer 2 Clicked")
                                    val answerOption2 = AnswerOption(answerOption2, true)

                                    stackAnswer[currentQuestionIndex] = answerOption2
                                    Log.d(TAG, "Stack : ${stackAnswer.size}")
                                    Log.d(TAG, "Stack : $stackAnswer")
                                    Log.d(TAG, "Stack : ${stackAnswer[currentQuestionIndex]}")

                                },
                                enabled = true
                            ){
                                Text(
                                    text = answerOption2,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxSize(),
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Answer 3
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor =if ( stackAnswer.getOrNull(currentQuestionIndex)?.text == answerOption3) colorResource(R.color.box3).copy(0.5f) else colorResource(R.color.box3),
                                ),
                                onClick = {
                                    Log.d(TAG, "Answer 3 Clicked")
                                    val answerOption3 = AnswerOption(answerOption3, true)

                                    stackAnswer[currentQuestionIndex] = answerOption3
                                    Log.d(TAG, "Stack : ${stackAnswer.size}")
                                    Log.d(TAG, "Stack : $stackAnswer")
                                    Log.d(TAG, "Stack : ${stackAnswer[currentQuestionIndex]}")

                                },
                                enabled = true
                            ){
                                Text(
                                    text = answerOption3,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxSize(),
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                            //Answer 4
                            Card (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if ( stackAnswer.getOrNull(currentQuestionIndex)?.text == answerOption4) colorResource(R.color.box4).copy(0.5f) else colorResource(R.color.box4),
                                ),
                                onClick = {
                                    Log.d(TAG, "Answer 4 Clicked")
                                    val answerOption4 = AnswerOption(answerOption4, true)

                                    stackAnswer[currentQuestionIndex] = answerOption4
                                    Log.d(TAG, "Stack : ${stackAnswer.size}")
                                    Log.d(TAG, "Stack : $stackAnswer")
                                    Log.d(TAG, "Stack : ${stackAnswer[currentQuestionIndex]}")

                                },
                                enabled = true
                            ){
                                Text(
                                    text = answerOption4,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxSize(),
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            Row (
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                // Button Prev
                                Button(
                                    modifier = Modifier
                                        .weight(0.15f)
                                        .fillMaxHeight(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.white),
                                        contentColor = colorResource(R.color.white)
                                    ),
                                    onClick = {
                                        Log.d(TAG, "Prev Button Clicked")
                                        currentQuestionIndex--

                                    },
                                    enabled = currentQuestionIndex > 0

                                ) {
                                    Text(
                                        text = "Prev",
                                        color = colorResource(R.color.blue),
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                //Button Selesai
                                Button(
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 2.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.DarkGray,
                                        contentColor = colorResource(R.color.white)
                                    ),
                                    onClick = {
                                        Log.d(TAG, "Submit Button Clicked")
                                        val hasEmptyAnswer = stackAnswer.any { it.text.isEmpty() }
                                        if (hasEmptyAnswer) {
                                            showDialogSubmit = true
                                            return@Button
                                        }else{

                                            qwizzzVM.countScore(stackAnswer)
                                            qwizzzVM.addScore()
                                            qwizzzVM.updateLeaderboard()
                                            navController.navigate(Screen.hasilAkhir.route){
                                                popUpTo(Screen.searchSelectQwizzz.route){
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    enabled = stackAnswer.size == questionSize

                                    ){
                                    Text(
                                        text = "Submit",
                                        color = colorResource(R.color.white),
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                //Button Next
                                Button(
                                    modifier = Modifier
                                        .weight(0.15f)
                                        .fillMaxHeight(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.white),
                                        contentColor = colorResource(R.color.white)
                                    ),
                                    onClick = {
                                        Log.d(TAG, "Next Button Clicked")
                                        currentQuestionIndex++

                                    },
                                    enabled = currentQuestionIndex < questionSize - 1

                                    ){
                                    Text(
                                        text = "Next",
                                        color = colorResource(R.color.blue),
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold
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