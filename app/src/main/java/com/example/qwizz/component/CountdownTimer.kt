package com.example.qwizz.component

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Preview(showBackground = true)
@Composable
fun CountDownTimer(
    totalTime: Int = 40,
    onTimeUp: () -> Unit = {}
) {
    var timeLeft by remember { mutableStateOf(totalTime) }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else {
            onTimeUp()
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Card(
        modifier = Modifier
            .padding(10.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.scrim,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp,
        )
    ) {

        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.headlineMedium,
            color = if (timeLeft <= 10 && timeLeft > 0 && timeLeft % 2 == 0) Color.White else if (timeLeft < 30) Color.Red else Color.White,
            modifier = Modifier.padding(8.dp)
        )
    }

}