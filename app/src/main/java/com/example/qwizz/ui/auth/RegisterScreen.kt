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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun RegisterScreen(
    navController: NavController,
){
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))

    val auth : FirebaseAuth = Firebase.auth
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember{ mutableStateOf(false) }

    var registerFailed by remember { mutableStateOf(false) }
    var usernameError by remember {mutableStateOf(false)}
    var emailError by remember {mutableStateOf(false)}
    var passwordError by remember {mutableStateOf(false)}
    var isLoading by remember { mutableStateOf(false)}


    val pressStart2P = FontFamily(
        Font(R.font.p2p_regular, FontWeight.Normal)
    )
    val roboto = FontFamily(
        Font(R.font.roboto_ligh, FontWeight.ExtraLight),
        Font(R.font.roboto_bold, FontWeight.ExtraBold)
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
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                )
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Register",
                            fontSize = 30.sp,
                            fontFamily = pottaOne,
                            fontWeight = FontWeight.Normal,

                            )
                    }

                    // Username Field
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = {
                            Text(
                                "Username",
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
                        isError = usernameError,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = colorResource(R.color.black),
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        },
                        singleLine = true
                    )

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
                        isError = passwordError,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = colorResource(R.color.black),
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        singleLine = true
                    )

                    //Button Register
                    Button(
                        modifier = Modifier
                            .width(130.dp)
                            .padding(5.dp)
                        ,
                        onClick = {
                            isLoading = true
                            registerFailed = false
                            usernameError = false
                            emailError = false
                            passwordError = false


                            Log.d("RegisterScreen", "Attempting registration with username: $userName, email: $email")

                            authViewModel.registerUser(userName, email, password){ success, message ->
                                if(success){
                                    Log.d("RegisterScreen", "Logout: ${auth.currentUser}")
                                    auth.signOut()
                                    Log.d("RegisterScreen", "Logout: ${auth.currentUser}")
                                    Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.loginScreen.route){
                                        popUpTo(Screen.loginScreen.route){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                    isLoading = false
                                    return@registerUser
                                }
                                else{
//                                    if(message == "Email tidak boleh kosong" || message == "Format email tidak valid"){
//                                        emailError = true
//                                    }
//                                    if(message == "Password tidak boleh kosong" || message == "Password minimal 8 karakter" || message == "Password harus berisi minimal 1 huruf besar, 1 huruf kecil, " + "1 angka, dan 1 karakter khusus (@#$%^&+=!)"){
//                                        passwordError = true
//                                    }
//                                    if(message == "Username tidak boleh kosong" || message == "Username hanya boleh berisi huruf, angka, dan karakter . _ -" + " dengan panjang 3-20 karakter"){
//                                        usernameError = true
//                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show().toString()
                                    emailError = true
                                    passwordError = true
                                    usernameError = true
                                    registerFailed = true
                                    isLoading = false
                                    return@registerUser
                                }
                            }

                        },
                        enabled = !isLoading

                        ) {
                        if(isLoading){
                            CircularProgressIndicator(
                                color = colorResource(R.color.white),
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }else{
                            Text(
                                text = "Register",
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
                            text = "Sudah Memiliki Akun?"

                        )
                        TextButton(onClick = {
                            navController.navigate("login_screen"){
                                popUpTo("login_screen"){
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(
                                text = "Login"
                            )
                        }
                    }

                }
            }
        }
    }

}