package com.example.qwizz.ui.doqwizz

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.viewmodel.makeqwizz.DoQwizzzViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay


@Composable
fun SearchSelectQwizzz(
    navController: NavController
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
                quiz.title.contains(searchQwizzz, ignoreCase = true) ||
                        quiz.topic.contains(searchQwizzz, ignoreCase = true) ||
                        quiz.name.contains(searchQwizzz, ignoreCase = true)
            }
        }
    }


    LaunchedEffect(Unit) {
        Log.d("SearchSelectQwizzz", "LaunchedEffect called")
        qwizzVM.fetchQuizzes()
        delay(1000)
        Log.d("SearchSelectQwizzz", "availibleQwizzz: $availibleQwizzz")
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
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
                                 navController.navigate(Screen.mainMenu.route)
                            }
                    )
                }

                SearchBar(
                    value = searchQwizzz,
                    onValueChange = { searchQwizzz = it },
                    onClick = {
                        /*TODO*/
                    }
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    thickness = 1.dp,
                    color = Color.DarkGray
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    itemsIndexed(filteredQwizzz) { index, quiz ->
                        CardQwizzz(
                            title = quiz.title,
                            topic = quiz.topic,
                            duration = quiz.timeQuiz.toString(),
                            author = quiz.name,
                            image = R.drawable.math_selecqwizz_icon,
                            onClick = {
                                Log.d("SearchSelectQwizzz", "Clicked on: ${quiz.title}")
                            }
                        )
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
