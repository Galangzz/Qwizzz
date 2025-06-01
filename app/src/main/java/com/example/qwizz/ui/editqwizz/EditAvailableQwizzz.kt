package com.example.qwizz.ui.editqwizz

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.colorspace.ColorModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.qwizz.component.AlertPerubahan
import com.example.qwizz.component.CardQwizzz
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.editqwizz.EditQwizzzViewModel
import kotlin.collections.random
import kotlin.contracts.contract

private const val TAG = "EditAvailableQwizzz"

@Preview (showBackground = true)
@Composable
fun EditAvailableQwizzz(
    navController: NavController = rememberNavController()
){
    val editVM: EditQwizzzViewModel = viewModel()
    val qwizzzList by editVM.quizList.collectAsState()
    val context = LocalContext.current

    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)

    val roboto = FontFamily(
        Font(R.font.roboto_reguler, FontWeight.Normal),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_ligh, FontWeight.Medium)
    )

    var isLoading by remember { mutableStateOf(false) }
    var alerted by remember { mutableStateOf(false) }
    var qwizzzChoose by remember { mutableStateOf<Qwizzz?>(null) }

    LaunchedEffect(key1 = Unit) {
        Log.d(TAG, "Fetching quizzes...")
        isLoading = true
        editVM.fetchQuizzesAvailables()
        isLoading = false
        Log.d(TAG, "Fetching quizzes finished")
        Toast.makeText(context, "Tahan lama untuk menghapus Quiz", Toast.LENGTH_LONG).show()

    }

    val mathIcon = listOf(
        R.drawable.math1,
        R.drawable.math2,
        R.drawable.math3,
        R.drawable.math4,
        R.drawable.math5,
        R.drawable.math6,
        R.drawable.math7,
    )

    val bahasaIcon = listOf(
        R.drawable.bahasa1,
        R.drawable.bahasa2,
        R.drawable.bahasa3,
        R.drawable.bahasa4,
        R.drawable.bahasa5,
        R.drawable.bahasa6,
        R.drawable.bahasa7
    )

    val topicIcon = mapOf(
        "Matematika" to mathIcon, "Bahasa" to bahasaIcon
    )



    BackHandler(enabled = true) {
        navController.navigate(Screen.SelectTopic.route) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    if (alerted){
        AlertPerubahan(
            title = "Hapus Quiz",
            text = "Apakah anda yakin ingin menghapus quiz: ${qwizzzChoose?.title}",
            onDismiss = {
                alerted = false
                qwizzzChoose = null
            },
            onConfirm = {
                editVM.deletedQwizzz(qwizzzChoose!!)
                alerted = false
                qwizzzChoose = null
            }

        )
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
                                navController.navigate(Screen.SelectTopic.route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                    )
                    Text(
                        text = "Pilih Qwizzz yang akan diedit",
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
                ){
                    if (isLoading){
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(
                                color = colorResource(R.color.white)
                            )
                        }
                    } else{
                        if (qwizzzList.isEmpty() && !isLoading){
                            Text("Belum ada kuis yang tersedia.")
                        }else{
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp, vertical = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                itemsIndexed(qwizzzList) { index, quiz ->
                                    val iconRes = topicIcon[quiz.topic]?.random() ?: R.drawable.math1
                                    val time = quiz.timeQuiz
                                    val duration = if (time > 60) {
                                        val minutes = time / 60
                                        val seconds = time % 60
                                        "$minutes Menit $seconds Detik"
                                    } else {
                                        "$time Detik"
                                    }
                                    CardQwizzz(
                                        title = quiz.title,
                                        topic = quiz.topic,
                                        duration = duration,
                                        author = quiz.name,
                                        image = iconRes,
                                        onClick = {
                                            Log.d(TAG, "Card clicked")
                                            editVM.updateCurrentAvailableQwizzz(quiz)
                                            Log.d(TAG, "Clicked on: ${quiz.title}")
                                            navController.navigate(Screen.editQwizzz.route){
                                                popUpTo(Screen.editAvailableQwizzz.route) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        },
                                        onLongClick = {
                                            Log.d(TAG, "Card long clicked")
                                            alerted = true
                                            qwizzzChoose = quiz
                                        }
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