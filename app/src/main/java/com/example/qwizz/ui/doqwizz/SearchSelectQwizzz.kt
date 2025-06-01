package com.example.qwizz.ui.doqwizz

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.CardQwizzz
import com.example.qwizz.component.SearchBar
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay


@Composable
fun SearchSelectQwizzz(
    navController: NavController,
    viwModel: DoQwizzzViewModel = viewModel()
) {
    val qwizzVM: DoQwizzzViewModel = viewModel()

    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)

    var searchQwizzz by remember { mutableStateOf("") }

    val availibleQwizzz by qwizzVM.quizList.collectAsState()

    val filteredQwizzz = remember(searchQwizzz, availibleQwizzz) {
        if (searchQwizzz.isBlank()) {
            availibleQwizzz
        } else {
            availibleQwizzz.filter { quiz ->
                quiz.title.contains(searchQwizzz, ignoreCase = true) || quiz.topic.contains(
                    searchQwizzz,
                    ignoreCase = true
                ) || quiz.name.contains(searchQwizzz, ignoreCase = true)
            }
        }
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

    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoaded = true
        Log.d("SearchSelectQwizzz", "LaunchedEffect called")
        try {
            qwizzVM.fetchQuizzes()

        } catch (e: Exception) {
            Log.e("SearchSelectQwizzz", "Error fetching quizzes", e)
        } finally {
            delay(500)
            isLoaded = false
            Log.d("SearchSelectQwizzz", "Loading finished")
        }

    }

    BackHandler(enabled = true) {
        navController.navigate(Screen.mainMenu.route) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center

        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = draw,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(
                modifier = Modifier.fillMaxSize(),
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
                                navController.navigate(Screen.mainMenu.route)
                            })
                }

                SearchBar(value = searchQwizzz, onValueChange = { searchQwizzz = it }, onClick = {
                    Log.d("SearchSelectQwizzz", "Search button clicked")

                })

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    thickness = 1.dp,
                    color = Color.DarkGray
                )
                if (isLoaded) {
                    Log.d("SearchSelectQwizzz", "Loading...")
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(
                            color = colorResource(R.color.white)
                        )
                    }
                } else {

                    if (filteredQwizzz.isEmpty() && searchQwizzz.isNotEmpty()) {
                        Text("Tidak ada kuis yang cocok dengan \"$searchQwizzz\".")
                    } else if (availibleQwizzz.isEmpty() && !isLoaded) {
                        Text("Belum ada kuis yang tersedia.")
                    } else{

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            itemsIndexed(filteredQwizzz) { index, quiz ->
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
                                        Log.d("SearchSelectQwizzz", "Card clicked")
                                        qwizzVM.updateCurrentQwizzz(quiz)
                                        Log.d("SearchSelectQwizzz", "Clicked on: ${quiz.title}")
                                        navController.navigate(Screen.initialDoQwizzz.route){
                                            popUpTo(Screen.searchSelectQwizzz.route) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    onLongClick = {
                                        Log.d("SearchSelectQwizzz", "Card long clicked")
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

//if else
//                    qwizzzList.forEach { dataItem ->
//                        CardQwizzz(
//                            title = dataItem.title,
//                            topic = dataItem.topic,
//                            duration = dataItem.duration,
//                            author = dataItem.author,
//                            icon = dataItem.iconRes,
//                            onClick = {
//                                println("Clicked on: ${dataItem.title}")
//                            }
//                        )
//                        // Anda mungkin ingin menambahkan Spacer atau Divider antar card di sini
//                        // Spacer(modifier = Modifier.height(8.dp))
//                    }
