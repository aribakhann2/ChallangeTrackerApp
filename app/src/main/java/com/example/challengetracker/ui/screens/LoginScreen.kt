package com.example.challengetracker.ui.screens

import ChallengeViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challengetracker.ui.theme.BlackBackground
import com.example.challengetracker.ui.theme.GraySecondary
import com.example.challengetracker.ui.theme.SkyBlueText
import com.example.challengetracker.ui.theme.WhiteText
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(navController: NavController, viewModel: ChallengeViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authError by viewModel.authError.collectAsState()
    val loggedInUserId by viewModel.loggedInUserId.collectAsState()

    LaunchedEffect(loggedInUserId) {

        if (loggedInUserId != null) {
            navController.navigate("challengeTracker")
        }
    }
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
                text = "Login",
                color = SkyBlueText,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            authError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            BasicTextField(
                value = email,
                onValueChange = {email=it},
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(WhiteText, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(text = "Email", color = GraySecondary, fontSize = 16.sp)
                        innerTextField()
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = password,
                onValueChange = {password=it},
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
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.loginUser(email, password)


                    } else {
                        viewModel.setAuthError("Fields cannot be empty")
                    }
                },
                colors = ButtonDefaults.run { buttonColors(SkyBlueText) }, // Sky blue button
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(50.dp) // Bigger button
            ) {
                Text(text = "Login", fontSize = 18.sp, color = GraySecondary)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("signup")
            }) {
                Text("Don't have an account? Sign up", color = SkyBlueText)
            }

        }
    }
}

