package com.example.challengetracker.ui.screens

import ChallengeViewModel
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.challengetracker.ui.theme.BlackBackground
import com.example.challengetracker.ui.theme.GraySecondary
import com.example.challengetracker.ui.theme.SkyBlueText
import com.example.challengetracker.ui.theme.WhiteText



@Composable
fun SignupScreen(navController: NavController, viewModel: ChallengeViewModel,

) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authError by viewModel.authError.collectAsState()
    val context = LocalContext.current

    //ui
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Sign Up",
                color = SkyBlueText,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (authError != null) {
                Text(
                    text = authError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            BasicTextField(
                value = username,
                onValueChange = {username=it
                    viewModel.setAuthError(null)},
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(WhiteText, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(text = "Username", color = GraySecondary, fontSize = 16.sp)
                        innerTextField()
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = password,
                onValueChange = {password=it
                    viewModel.setAuthError(null)},
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(WhiteText, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(text = "Password", color = GraySecondary, fontSize = 16.sp)
                        innerTextField()
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            BasicTextField(
                value = confirmPassword,
                onValueChange = {confirmPassword=it
                    viewModel.setAuthError(null)},
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(WhiteText, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(text = "Confirm Password", color = GraySecondary, fontSize = 16.sp)
                        innerTextField()
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (password == confirmPassword) {
                            viewModel.checkUsernameAndSignUp(username, password) { success ->
                                if (success) {
                                    navController.navigate("challengeTracker")
                                    Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            viewModel.setAuthError("Passwords do not match")
                        }
                    } else {
                        viewModel.setAuthError("Please fill out all the fields")
                    }
                },
                colors = ButtonDefaults.run { buttonColors(SkyBlueText) }, // Sky blue button
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(50.dp) // Bigger button
            ) {
                Text(text = "shah ameer", fontSize = 18.sp, color = GraySecondary)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("login")}) {
                Text("Already have an account? Log in", color = SkyBlueText)
            }

        }
    }
}

