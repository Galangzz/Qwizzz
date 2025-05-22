package com.example.qwizz.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.qwizz.R
import com.example.qwizz.Screen
import com.example.qwizz.viewmodel.auth.AuthViewModel
import com.example.qwizz.viewmodel.auth.AuthViewModelFactory


@Composable
fun LoginScreen(
    navController: NavController,

){
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember{ mutableStateOf(false) }
    var emailError by remember{ mutableStateOf(false) }
    var isCheckboxChecked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false)}

    var rememberMeChecked by remember { mutableStateOf(false) }


    val pressStart2P = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )

    val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.ExtraBold),
        Font(R.font.roboto_ligh, FontWeight.ExtraLight)
    )

    val pottaOne = FontFamily(
        Font(R.font.pottaone, FontWeight.Normal)
    )

    val draw = painterResource(R.drawable.bg)

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.TopCenter

    ){
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = draw,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = "Qwizzz.",
                    fontSize = 24.sp,
                    fontFamily = pressStart2P,
                    modifier = Modifier,
                    color = colorResource(R.color.blue)
                )
            }

            Spacer(
                modifier = Modifier
                    .height(150.dp)
            )

            Card (
                modifier = Modifier
                    .width(310.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.blue_rect)
                ),

                ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Login",
                            fontSize = 30.sp,
                            fontFamily = pottaOne,
                            fontWeight = FontWeight.Normal,

                            )
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Email",
                                color = colorResource(R.color.black),
                                fontFamily = roboto,
                                fontWeight = FontWeight.ExtraLight
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colorResource(R.color.black),
                            unfocusedTextColor = colorResource(R.color.black),
                            unfocusedBorderColor = colorResource(R.color.black),
                            focusedBorderColor = colorResource(R.color.black_shadow)
                        ),
                        isError = emailError,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = colorResource(R.color.black),
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        },

                        singleLine = true
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                "Password",
                                color = colorResource(R.color.black),
                                fontFamily = roboto,
                                fontWeight = FontWeight.ExtraLight
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colorResource(R.color.black),
                            unfocusedTextColor = colorResource(R.color.black),
                            unfocusedBorderColor = colorResource(R.color.black),
                            focusedBorderColor = colorResource(R.color.black_shadow)
                        ),
                        isError = password.isNotEmpty() && password.length < 8,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = colorResource(R.color.black),
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        },
                        singleLine = true
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        isCheckboxChecked = false
                        Checkbox(
                            checked = rememberMeChecked,
                            onCheckedChange = {isChecked ->
                                rememberMeChecked = isChecked

                                if(!isChecked){
                                    isCheckboxChecked = false
                                    authViewModel.clearSharedPreferences()
                                    Log.d("LoginScreen", "User removed from shared preferences, remember: ${authViewModel.isRemembered()}")

                                }else{
                                    isCheckboxChecked = true
                                    authViewModel.saveUserToSharedPreferences(isCheckboxChecked)
                                    Log.d("LoginScreen", "User saved to shared preferences, remember: ${authViewModel.isRemembered()}")
                                }
                            }
                        )
                        Text(
                            text = "Remember Me",
                            color = colorResource(R.color.black))
                    }
                    Button(
                        modifier = Modifier
                            .width(110.dp)
                            .padding(5.dp)
                        ,
                        onClick = {
                            isLoading = true
                            emailError = false

                            if(email.isEmpty()){
                                Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                emailError = true
                                isLoading = false
                                return@Button
                            }
                            if(password.isEmpty()){
                                Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                isLoading = false
                                return@Button
                            }
                            authViewModel.loginUser(email, password){ success, message ->
                                if(success){
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.mainMenu.route){
                                        popUpTo(0){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                                else{
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    if (message == "Format email tidak valid"){
                                        emailError = true
                                    }
                                }

                            }

                        },
                        enabled = !isLoading,

                        ) {
                        if(isLoading){
                            CircularProgressIndicator(
                                color = colorResource(R.color.white),
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }else{
                            Text(
                                text = "Login",
                                color = colorResource(R.color.white),
                                fontFamily = roboto,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            modifier = Modifier,
                            text = "Belum memiliki akun?"

                        )
                        TextButton(
                            onClick = {
                                navController.navigate(Screen.registerScreen.route)
                            }
                        ) {
                            Text(
                                text = "Register"
                            )
                        }
                    }

                }
            }
        }


    }
}