package com.example.qwizz.ui.editqwizz

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.AlertPerubahan
import com.example.qwizz.component.ConfirmTime
import com.example.qwizz.component.InputAnswerComponent
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.viewmodel.editqwizz.EditQwizzzViewModel

private const val TAG = "EditQwizzz"

@Preview(showBackground = true)
@Composable
fun EditQwizzz(
    navController: NavController = rememberNavController(),
    vm: EditQwizzzViewModel = viewModel()
) {

    val qwizzz by vm.currentQwizzz.collectAsState()

    Log.d(TAG, "Qwizzz: $qwizzz")
    val draw = painterResource(R.drawable.bg)
    val backIcon = painterResource(R.drawable.back_icon)
    val trash = Icons.Default.Delete
    val context = LocalContext.current

    val roboto = FontFamily(
        Font(R.font.roboto_reguler, FontWeight.Normal),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_ligh, FontWeight.Medium)
    )

    var alertDelete by remember { mutableStateOf(false) }
    var indexDeleted by remember { mutableIntStateOf(-1) }
    var alertConfirmTime by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    if (alertDelete) {
        AlertPerubahan(
            title = "Peringatan",
            text = "Apakah anda yakin ingin menghapus pertanyaan ini?",
            onDismiss = {
                alertDelete = false
            },
            onConfirm = {
                alertDelete = false
                vm.deleteQuestionAt(0, indexDeleted)
                indexDeleted = -1
            }
        )
    }

    if (alertConfirmTime){
        ConfirmTime(
            onDismiss = {
                alertConfirmTime = false
            },
            onConfirm = { totalDetik ->
                if(totalDetik <= 0){
                    Toast.makeText(context, "Waktu harus lebih dari 0 detik", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("InputQuestion", "Total Detik: $totalDetik")
                    vm.updateTime(totalDetik)

                    isLoading = true
                    vm.updateQwizzz{ success ->
                        isLoading = false
                        if(success){
                            Toast.makeText(context, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.editAvailableQwizzz.route) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }else{
                            Toast.makeText(context, "Gagal disimpan", Toast.LENGTH_SHORT).show()
                        }

                    }


                }
            }
        )
    }


    BackHandler(enabled = true) {
        navController.navigate(Screen.editAvailableQwizzz.route) {
            popUpTo(0) {
                inclusive = true
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
                                navController.navigate(Screen.editAvailableQwizzz.route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                    )
                    Text(
                        text = "Edit:",
                        fontFamily = roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(R.color.black),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    BasicTextField(
                        value = qwizzz?.title ?: "",
                        onValueChange = { newText ->
                            vm.updateTitleQwizzz(newText)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        textStyle = TextStyle(
                            color = colorResource(R.color.black),
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 5.dp
                                        ),
                                    contentAlignment = Alignment.CenterStart
                                ){
                                    if (qwizzz?.title?.isEmpty() == true) {
                                        Text(
                                            text = "Ketik Judul",
                                            fontFamily = roboto,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                        )
                                    }
                                    innerTextField()
                                }

                            }

                        }

                    )


                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        itemsIndexed(qwizzz?.question ?: emptyList()) { index, qwizzz ->

                            // Card Edit
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 40.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(R.color.blue_card_main)
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 20.dp,
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = trash,
                                        contentDescription = null,
                                        tint = colorResource(R.color.white),
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clickable {
                                                //TODO: Delete
                                                alertDelete = true
                                                indexDeleted = index

                                            }
                                    )
                                    BasicTextField(
                                        // TODO: Question 1
                                        value = qwizzz.questionText,
                                        onValueChange = { newText ->
                                            vm.updateQwizQuestion(index, newText)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                            .background(
                                                color = if (qwizzz.questionText.isEmpty()) colorResource(
                                                    R.color.black_shadow
                                                ) else colorResource(
                                                    R.color.blue_head_up
                                                ),
                                                shape = RoundedCornerShape(18.dp)
                                            ),
                                        decorationBox = { innerTextField ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(15.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(
                                                            horizontal = 10.dp,
                                                            vertical = 5.dp
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (qwizzz.questionText.isEmpty()) {
                                                        Text(
                                                            text = "Ketik Pertanyaan",
                                                            color = colorResource(R.color.black),
                                                            fontFamily = roboto,
                                                            fontWeight = FontWeight.Normal,
                                                            fontSize = 16.sp
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                            }
                                        },
                                    )
                                    //Box Comp 1
                                    InputAnswerComponent(
                                        // TODO: Answer 1
                                        value = qwizzz.options.getOrElse(0) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.text,
                                        onValueChange = { newText ->
                                            vm.updateAnswerText(index, 0, newText)
                                        },
                                        isCorrect = qwizzz.options.getOrElse(0) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.correct,
                                        onIsCorrectChange = { isChecked ->
                                            vm.updateAnswerCorrect(index, 0)
                                        },
                                        colorBox = R.color.box1,
                                        enabled = true,
                                        context = context
                                    )

                                    // Box Comp 2
                                    InputAnswerComponent(
                                        // TODO: Answer 2
                                        value = qwizzz.options.getOrElse(1) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.text,
                                        onValueChange = { newText ->
                                            vm.updateAnswerText(index, 1, newText)
                                        },
                                        isCorrect = qwizzz.options.getOrElse(1) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.correct,
                                        onIsCorrectChange = { isChecked ->
                                            vm.updateAnswerCorrect(index, 1)
                                        },
                                        colorBox = R.color.box2,
                                        enabled = true,
                                        context = context
                                    )

                                    // Box Comp 3
                                    InputAnswerComponent(
                                        // TODO: Answer 3
                                        value = qwizzz.options.getOrElse(2) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.text,
                                        onValueChange = { newText ->
                                            vm.updateAnswerText(index, 2, newText)
                                        },
                                        isCorrect = qwizzz.options.getOrElse(2) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.correct,
                                        onIsCorrectChange = { isChecked ->
                                            vm.updateAnswerCorrect(index, 2)
                                        },
                                        colorBox = R.color.box3,
                                        enabled = true,
                                        context = context
                                    )

                                    // Box Comp 4
                                    InputAnswerComponent(
                                        // TODO: Answer 4
                                        value = qwizzz.options.getOrElse(3) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.text,
                                        onValueChange = { newText ->
                                            vm.updateAnswerText(index, 3, newText)
                                        },
                                        isCorrect = qwizzz.options.getOrElse(3) {
                                            AnswerOption(
                                                "",
                                                false
                                            )
                                        }.correct,
                                        onIsCorrectChange = { isChecked ->
                                            vm.updateAnswerCorrect(index, 3)
                                        },
                                        colorBox = R.color.box4,
                                        enabled = true,
                                        context = context
                                    )
                                }
                            }
                        }



                        item {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = colorResource(R.color.white),
                                    modifier = Modifier
                                        .size(30.dp)
                                        .border(
                                            1.dp,
                                            colorResource(R.color.white),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable(
                                            onClick = {
                                                vm.addEmptyQwizzzQuestion()
                                            }
                                        ),

                                    )
                                Button(
                                    onClick = {
                                        if (vm.isQuizEmpty()) {
                                            vm.deletedQwizzz(qwizzz)
                                            navController.navigate(Screen.editAvailableQwizzz.route) {
                                                popUpTo(0) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        } else {
                                            if (vm.isValidQuiz()) {
                                                alertConfirmTime = true
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Pastikan semua pertanyaan diisi, jawaban tidak duplikat, dan ada satu yang benar",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@Button
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 70.dp, vertical = 15.dp),
                                    shape = RectangleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.blue_head_up),
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 10.dp,
                                        pressedElevation = 20.dp
                                    )
                                ) {
                                    Text(
                                        text = "Simpan",
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        style = TextStyle(
                                            color = colorResource(R.color.black),
                                            shadow = Shadow(
                                                color = colorResource(R.color.teal_700),
                                                offset = Offset(2f, 4f),
                                                blurRadius = 4f
                                            )
                                        ),
                                        modifier = Modifier
                                            .padding(vertical = 10.dp)
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
