package com.example.qwizz.ui.doqwizz

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.CardQwizzz
import com.example.qwizz.component.SearchBar
import com.example.qwizz.component.TitleComponent


@Composable
fun SearchSelectQwizzz(
    navController: NavController
) {

    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)

    var searchQwizzz by remember { mutableStateOf("") }

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
//                    .verticalScroll(rememberScrollState()),
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

                    items(10) { index ->
                        CardQwizzz(
                            title = "Perkalian dan Pembagian",
                            topic = "Matematika",
                            duration = "20",
                            author = "Galang",
                            image = R.drawable.math_selecqwizz_icon,
                            onClick = {}
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
