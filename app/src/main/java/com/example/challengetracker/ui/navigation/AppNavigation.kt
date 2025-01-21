package com.example.challengetracker.ui.navigation

import ChallengeViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challengetracker.ui.screens.ChallengeTrackerScreen
import com.example.challengetracker.ui.screens.LoginScreen
import com.example.challengetracker.ui.screens.NavigationScreen
import com.example.challengetracker.ui.screens.SignupScreen


@Composable
fun AppNavigation(viewmodel:ChallengeViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "challengeTracker"
    ) {
        composable("challengeTracker") { ChallengeTrackerScreen(
            navController,
            viewModel = viewmodel
        ) }
        composable("signup") {
            SignupScreen(
                navController,
                viewModel=viewmodel
            )
        }
        composable("login") {
            LoginScreen(
                navController,
                viewModel = viewmodel
            )
        }
        composable("navigation") { NavigationScreen(navController) }
    }}