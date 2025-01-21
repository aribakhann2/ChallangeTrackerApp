package com.example.challengetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.challengetracker.ui.theme.BlackBackground
import com.example.challengetracker.ui.theme.GraySecondary
import com.example.challengetracker.ui.theme.SkyBlueText

import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun NavigationScreen(navController: NavController) {
    // State to handle confirmation dialog visibility
    var showLogoutDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Profile Section
        Text(
            text = "Keep Tracking",
            style = MaterialTheme.typography.displaySmall,
            color = SkyBlueText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Links
        Button(
            onClick = { navController.navigate("challengeTracker") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
        ) {
            Text("Challenge Tracker", color = GraySecondary, style = MaterialTheme.typography.titleMedium)
        }

        Button(
            onClick = { navController.navigate("signup") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
        ) {
            Text("Sign Up", color = GraySecondary)
        }

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
        ) {
            Text("Login", color = GraySecondary)
        }


        // Logout Button
        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
        ) {
            Text("Logout", color = GraySecondary)
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Confirm Logout", color = SkyBlueText) },
                text = { Text("Are you sure you want to log out?", color = GraySecondary) },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            navController.navigate("login") {
                                // Clear back stack to prevent going back after logout
                                popUpTo("navigationScreen") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
                    ) {
                        Text("Yes", color = GraySecondary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("No", color = SkyBlueText)
                    }
                }
            )
        }
    }
}

