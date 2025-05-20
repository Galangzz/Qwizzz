package com.example.qwizz.ui.makeqwizz

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.component.AlertPerubahan
import com.example.qwizz.component.InputAnswerComponent
import com.example.qwizz.component.TitleComponent
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.data.model.QuizQuestion
import com.example.qwizz.viewmodel.makeqwizz.MakeQwizzzViewModel

//@Preview (showBackground = true)
@Composable
fun InputQuestion(
    navController: NavController,
    topic : String,
    title : String,
    qwizzViewModel: MakeQwizzzViewModel = viewModel()
){
    val topic = topic
    val title = title
    Log.d("InputQuestion", "Topic: $topic, Title: $title")
    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_reguler, FontWeight.Normal)
    )
    val p2p = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal),
    )



    val context = LocalContext.current

    val draw = painterResource(R.drawable.bg)
    val back_icon = painterResource(R.drawable.back_icon)

    var showDialog by remember { mutableStateOf(false) }
    var dialogBack by remember { mutableStateOf(false) }
    var dialogSelesai by remember { mutableStateOf(false) }

    var question by remember { mutableStateOf("") }
    var answer1 by remember { mutableStateOf("") }
    var answer2 by remember { mutableStateOf("") }
    var answer3 by remember { mutableStateOf("") }
    var answer4 by remember { mutableStateOf("") }

    var countCheck by remember { mutableIntStateOf(0) }

    var isAnswer1Correct by remember { mutableStateOf(false) }
    var isAnswer2Correct by remember { mutableStateOf(false) }
    var isAnswer3Correct by remember { mutableStateOf(false) }
    var isAnswer4Correct by remember { mutableStateOf(false) }

    var allQuizQuestion = remember { mutableListOf<QuizQuestion>() }
    var currentEditingIndex by remember { mutableIntStateOf(-1) }




    fun loadQuestionDataIntoUI(index:Int){
        if(index >= 0 && index < allQuizQuestion.size && allQuizQuestion[index].questionText.isNotEmpty()){
            val questionToLoad = allQuizQuestion[index]
            question = questionToLoad.questionText
            answer1 = questionToLoad.options.getOrElse(0) { AnswerOption("", false) }.text
            answer2 = questionToLoad.options.getOrElse(1) { AnswerOption("", false) }.text
            answer3 = questionToLoad.options.getOrElse(2) { AnswerOption("", false) }.text
            answer4 = questionToLoad.options.getOrElse(3) { AnswerOption("", false) }.text
            isAnswer1Correct = questionToLoad.options.getOrElse(0) { AnswerOption("", false) }.isCorrect
            isAnswer2Correct = questionToLoad.options.getOrElse(1) { AnswerOption("", false) }.isCorrect
            isAnswer3Correct = questionToLoad.options.getOrElse(2) { AnswerOption("", false) }.isCorrect
            isAnswer4Correct = questionToLoad.options.getOrElse(3) { AnswerOption("", false) }.isCorrect
            countCheck = if (isAnswer1Correct || isAnswer2Correct || isAnswer3Correct || isAnswer4Correct) 1 else 0
        }else{
            Log.e("InputQuestion", "Invalid index: $index")

        }
    }

    fun resetUIForNewQuestion(){
        question = ""
        answer1 = ""
        answer2 = ""
        answer3 = ""
        answer4 = ""
        isAnswer1Correct = false
        isAnswer2Correct = false
        isAnswer3Correct = false
        isAnswer4Correct = false
        countCheck = 0
    }

    if (showDialog) {
        AlertPerubahan(
            title = "Peringatan",
            text = "Perubahan belum disimpan, apakah anda yakin ingin keluar?",
            onDismiss = {
                showDialog = false // Dismiss the dialog
            },
            onConfirm = {
                showDialog = false // Dismiss the dialog
                allQuizQuestion.clear()
                currentEditingIndex = -1
                resetUIForNewQuestion()
                Log.d("InputQuestion", "All questions cleared")
                navController.navigate(Screen.SelectTopic.route)
            }
        )
    }



    Surface(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center,

        ){
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
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = back_icon,
                        contentDescription = null,
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                if (allQuizQuestion.isEmpty() && question.isEmpty() && answer1.isEmpty() && answer2.isEmpty() && answer3.isEmpty() && answer4.isEmpty() && countCheck == 0){
                                    navController.navigate(Screen.SelectTopic.route)
                                }else{
                                    // Alert
                                    Log.d("InputQuestion", "Alert")
                                    showDialog = true
                                    dialogBack = true
                                    dialogSelesai = false
                                }
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(remember { ScrollState(0) })
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row (
                        modifier = Modifier
                            .width(320.dp)
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = title,
                            fontFamily = p2p,
                            color = colorResource(R.color.white),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier,
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(R.color.white),
                                shadow = Shadow(
                                    color = colorResource(R.color.black_shadow),
                                    offset = Offset(5f, 5f),
                                    blurRadius = 0.1f,
                                )
                            )
                        )
                    }

                    Card (
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
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            BasicTextField(
                                value = question,
                                onValueChange = { question = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .height(180.dp)
                                    .background(
                                        color = if (question.isEmpty()) colorResource(R.color.black_shadow) else colorResource(
                                            R.color.blue_head_up
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    ),
                                decorationBox = { innerTextField ->
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 10.dp, vertical = 5.dp),
                                            contentAlignment = Alignment.Center
                                        ){
                                            if(question.isEmpty()){
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
                                value = answer1,
                                onValueChange = { answer1 = it },
                                isCorrect = isAnswer1Correct,
                                onIsCorrectChange = { isChecked ->
                                    isAnswer1Correct = isChecked
                                    countCheck = if (isChecked) 1 else 0
                                },
                                colorBox = R.color.box1,
                                enabled = countCheck == 0 || isAnswer1Correct,
                                context = context
                            )


                            // Box Comp 2
                            InputAnswerComponent(
                                value = answer2,
                                onValueChange = { answer2 = it },
                                isCorrect = isAnswer2Correct,
                                onIsCorrectChange = { isChecked ->
                                    isAnswer2Correct = isChecked
                                    countCheck = if (isChecked) 1 else 0
                                },
                                colorBox = R.color.box2,
                                enabled = countCheck == 0 || isAnswer2Correct,
                                context = context
                            )

                            //Box Comp 3
                            InputAnswerComponent(
                                value = answer3,
                                onValueChange = { answer3 = it },
                                isCorrect = isAnswer3Correct,
                                onIsCorrectChange = { isChecked ->
                                    isAnswer3Correct = isChecked
                                    countCheck = if (isChecked) 1 else 0
                                },
                                colorBox = R.color.box3,
                                enabled = countCheck == 0 || isAnswer3Correct,
                                context = context
                            )

                            //Box Comp 4
                            InputAnswerComponent(
                                value = answer4,
                                onValueChange = { answer4 = it },
                                isCorrect = isAnswer4Correct,
                                onIsCorrectChange = { isChecked ->
                                    isAnswer4Correct = isChecked
                                    countCheck = if (isChecked) 1 else 0
                                },
                                colorBox = R.color.box4,
                                enabled = countCheck == 0 || isAnswer4Correct,
                                context = context
                            )



                            ///
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                //Button Prev
                                Button(
                                    modifier = Modifier
                                        .width(80.dp)
                                    ,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.white),
                                        contentColor = colorResource(R.color.white)

                                    ),
                                    onClick = {
                                        //DO something
                                        Log.d("InputQuestion", "Button Prev Clicked")
                                        if(currentEditingIndex >= 0){
                                            loadQuestionDataIntoUI(currentEditingIndex)
                                            countCheck = if (isAnswer1Correct || isAnswer2Correct || isAnswer3Correct || isAnswer4Correct) 1 else 0
                                            currentEditingIndex--
                                            Log.d("InputQuestion", "Current Editing Index: $currentEditingIndex")
                                            return@Button
                                        }

                                    },
                                    enabled = allQuizQuestion.isNotEmpty() && currentEditingIndex >= 0
                                ){
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
                                        .width(100.dp)
                                    ,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.purple_200),
                                        contentColor = colorResource(R.color.white)

                                    ),
                                    onClick = {
                                        Log.d("InputQuestion", "Button Selesai Clicked")
                                        var nextIndex = if (currentEditingIndex == -1) 0 else currentEditingIndex + 1
                                        if (nextIndex >= allQuizQuestion.size){
                                            if ((question.isEmpty() && answer1.isEmpty() && answer2.isEmpty() && answer3.isEmpty() && answer4.isEmpty() )&& countCheck == 0){
                                                qwizzViewModel.saveQwizzzToDB(topic, title, allQuizQuestion){
                                                        success, message ->
                                                    if (success){
                                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                        navController.navigate(Screen.SelectTopic.route){
                                                            popUpTo(Screen.SelectTopic.route){
                                                                inclusive = true
                                                            }
                                                            launchSingleTop = true
                                                        }
                                                    }else{
                                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                        Log.d("InputQuestion", "Error: $message")
                                                    }
                                                }
                                            }else{
                                                if((question.isNotEmpty() || answer1.isNotEmpty() || answer2.isNotEmpty() || answer3.isNotEmpty() || answer4.isNotEmpty()) && countCheck == 0 ){
                                                    Log.d("InputQuestion", "Semua pertanyaan harus diisi")
                                                    Toast.makeText(context, "Semua pertanyaan harus diisi dan Pilih Jawaban yang benar", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }else{
                                                    Log.d("InputQuestion", " Semua pertanyaan sudah diisi dan benar")

                                                    // add db
                                                    qwizzViewModel.saveQwizzzToDB(topic, title, allQuizQuestion){
                                                            success, message ->
                                                        if (success){
                                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                            navController.navigate(Screen.SelectTopic.route){
                                                                popUpTo(Screen.SelectTopic.route){
                                                                    inclusive = true
                                                                }
                                                                launchSingleTop = true
                                                            }
                                                        }else{
                                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                            Log.d("InputQuestion", "Error: $message")
                                                        }
                                                    }
                                                }

                                            }
                                        } else {
                                            val updatedQuestion = QuizQuestion(
                                                questionText = question,
                                                options = listOf(
                                                    AnswerOption(answer1, isAnswer1Correct),
                                                    AnswerOption(answer2, isAnswer2Correct),
                                                    AnswerOption(answer3, isAnswer3Correct),
                                                    AnswerOption(answer4, isAnswer4Correct)
                                                )
                                            )
                                            Log.d("InputQuestion", "Attempted to add question")
                                            allQuizQuestion[nextIndex] = updatedQuestion

                                            qwizzViewModel.saveQwizzzToDB(topic, title, allQuizQuestion){
                                                    success, message ->
                                                if (success){
                                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                    navController.navigate(Screen.SelectTopic.route){
                                                        popUpTo(Screen.SelectTopic.route){
                                                            inclusive = true
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }else{
                                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                    Log.d("InputQuestion", "Error: $message")
                                                }
                                            }

                                        }




                                        ////////

                                    },
                                    enabled = allQuizQuestion.isNotEmpty()
                                ){
                                    Text(
                                        text = "Selesai",
                                        color = colorResource(R.color.white),
                                        fontFamily = roboto,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                //Button Next
                                Button(
                                    modifier = Modifier
                                        .width(80.dp)
                                    ,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.white),
                                        contentColor = colorResource(R.color.white)

                                    ),
                                    onClick = {
                                        //DO something
                                        Log.d("InputQuestion", "Button Next Clicked")
                                        if(question.isEmpty() || answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty()){
                                            Toast.makeText(context, "Semua pertanyaan harus diisi", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        if (countCheck < 1){
                                            Toast.makeText(context, "Pilih jawaban yang benar", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        var nextIndex = if (currentEditingIndex == -1) 0 else currentEditingIndex + 1
                                        Log.d("InputQuestion", "Next Index: $nextIndex")
                                        if (nextIndex >= allQuizQuestion.size) {
                                            val updatedQuestion = QuizQuestion(
                                                questionText = question,
                                                options = listOf(
                                                    AnswerOption(answer1, isAnswer1Correct),
                                                    AnswerOption(answer2, isAnswer2Correct),
                                                    AnswerOption(answer3, isAnswer3Correct),
                                                    AnswerOption(answer4, isAnswer4Correct)
                                                )
                                            )
                                            Log.d("InputQuestion", "Attempted to add question")
                                            allQuizQuestion.add(updatedQuestion)
                                            Log.d("InputQuestion", "Question added successfully")
                                            resetUIForNewQuestion()
                                            Log.d("InputQuestion", "Reset UI called")
                                            Toast.makeText(context, "Pertanyaan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                            Log.d("InputQuestion", "Pertanyaan: ${allQuizQuestion[nextIndex].questionText}")
                                            Log.d("InputQuestion", "Jawaban: ${allQuizQuestion[nextIndex].options}")
                                            currentEditingIndex++
                                            Log.d("InputQuestion", "Current Editing Index: $currentEditingIndex")
                                            return@Button
                                        }else{
                                            Log.d("InputQuestion", "Attempted to update question")
                                            Log.d("InputQuestion", "Current Editing Index: $currentEditingIndex")
                                            //add
                                            if (nextIndex >= 0 && nextIndex < allQuizQuestion.size) {
                                                val updatedQuestion = QuizQuestion(
                                                    questionText = question,
                                                    options = listOf(
                                                        AnswerOption(answer1, isAnswer1Correct),
                                                        AnswerOption(answer2, isAnswer2Correct),
                                                        AnswerOption(answer3, isAnswer3Correct),
                                                        AnswerOption(answer4, isAnswer4Correct)
                                                    )
                                                )
                                                allQuizQuestion[nextIndex] = updatedQuestion
                                                Log.d("InputQuestion", "Question updated successfully")

                                            }else{
                                                Log.d("InputQuestion", "Invalid current editing index")
                                                return@Button
                                            }

                                            Toast.makeText(context, "Pertanyaan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                            currentEditingIndex++
                                            //reset
                                            resetUIForNewQuestion()
                                            //load
                                            loadQuestionDataIntoUI(nextIndex+1)
                                            return@Button
                                        }
                                    },
                                    enabled = question.isNotEmpty() && answer1.isNotEmpty() && answer2.isNotEmpty() && answer3.isNotEmpty() && answer4.isNotEmpty() && countCheck >= 1
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

    BackHandler(enabled = true) {
        if (allQuizQuestion.isEmpty() && question.isEmpty() && answer1.isEmpty() && answer2.isEmpty() && answer3.isEmpty() && answer4.isEmpty() && countCheck == 0) {
            Log.d("InputQuestion", "Back pressed")
            showDialog = false
            navController.navigate(Screen.SelectTopic.route)
        } else {
            showDialog = true
            dialogBack = true

        }
    }
}


