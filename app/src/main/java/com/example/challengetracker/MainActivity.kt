package com.example.challengetracker

import ChallengeViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.challengetracker.ui.navigation.AppNavigation
import com.example.challengetracker.ui.screens.ChallengeTrackerScreen
import com.example.challengetracker.ui.screens.LoginScreen
import com.example.challengetracker.ui.screens.SignupScreen
import com.example.challengetracker.ui.theme.ChallengeTrackerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
             var challengeViewModel: ChallengeViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ChallengeViewModel::class.java]

            ChallengeTrackerTheme {
                AppNavigation(viewmodel=challengeViewModel)
            }
        }
    }
}
