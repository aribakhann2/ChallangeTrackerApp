package com.example.challengetracker.ui.screens

import ChallengeViewModel
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

import com.example.challengetracker.entity.Challenge
import com.example.challengetracker.ui.theme.GraySecondary
import com.example.challengetracker.ui.theme.SkyBlueText
import com.example.challengetracker.ui.theme.WhiteText


@Composable
fun ChallengeTrackerScreen(navController: NavController, viewModel: ChallengeViewModel) {
    val challengeList by viewModel.combinedChallenges.collectAsState()
    val loggedInUserId by viewModel.loggedInUserId.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val defaultChallenges by viewModel.defaultChallenges.observeAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var newChallengeName by remember { mutableStateOf("") }
    var newChallengeDescription by remember { mutableStateOf("") }
    var newChallengeType by remember { mutableStateOf("") }
    var newChallengeDuration by remember { mutableStateOf("0") }
    var selectedImage by remember { mutableStateOf<String>("") }
    val completedChallengesCount = challengeList.count { it.isCompleted }
    val progress = if (challengeList.count() > 0) {
        completedChallengesCount.toFloat() / challengeList.count()
    } else {
        0f // Avoid division by zero
    }
    var isMenuOpen by rememberSaveable { mutableStateOf(false) } // Use rememberSaveable for persistence

    Scaffold(
        topBar = {
            TopAppBar(
                //title = { Text("Keep Tracking", color = GraySecondary) },
                title = { Spacer(modifier = Modifier) },
                backgroundColor = SkyBlueText,
              //  modifier = Modifier.statusBarsPadding(),
              //  Spacer(modifier = Modifier.weight(1.5f)),
                navigationIcon = {
                    IconButton(onClick = {
                        isMenuOpen = !isMenuOpen
                        if (isMenuOpen) {
                            navController.navigate("navigation") // Navigate to the profile screen
                        } else {
                            navController.popBackStack() // Navigate back to the previous screen
                        }
                    }) {
                        Icon(
                            imageVector = if (isMenuOpen) Icons.Default.Close else Icons.Default.Menu, // Toggle between Menu and Close
                            contentDescription = if (isMenuOpen) "Close Menu" else "Open Menu",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Hero section with background image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Increased height for the hero section
                    .background(SkyBlueText)
                    .padding( horizontal = 26.dp,vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row{

                        Text( text = "Keep Tracking",
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            shadow = Shadow(
                                color = Color.Gray,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            ),
                            fontFamily = FontFamily.Serif
                        ),
                        color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row{
                        Text(text="1 challange",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text="Your Goal:",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black)
                    }
                    Spacer(modifier = Modifier.weight(0.25f)) // Pushes progress bar to the bottom of the box
                    Row{
                        LinearProgressIndicator(
                            progress = progress, // Replace with actual progress (e.g., completedChallenges / totalChallenges)
                            modifier = Modifier.width(300.dp)
                                .height(25.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color.Green,
                            backgroundColor = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text="${completedChallengesCount} / ${challengeList.count()}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black)
                    }
                    // Progress bar

                }
            }

            // Button to create a new challenge
            Button(
                onClick = {
                   if (loggedInUserId != null) {
                        showDialog = true
                    } else {
                        Toast.makeText(
                            navController.context,
                            "You must be logged in to create a challenge.",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("login")
                   }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
            ) {
                Text("Create a Challenge", color = GraySecondary)
            }

            // Challenge List displayed as cards
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(challengeList) { challenge ->
                    ChallengeCard(challenge, viewModel)
                }
            }

            // Show create challenge form when the button is clicked
            if (showDialog) {
                CreateChallengeDialog(
                    onDismiss = { showDialog = false },
                    newChallengeName = newChallengeName,
                    newChallengeDescription = newChallengeDescription,
                    newChallengeDuration = newChallengeDuration,
                    onNameChange = { newChallengeName = it },
                    onDescriptionChange = { newChallengeDescription = it },
                    onDurationChange = { newChallengeDuration = it },
                    onCreate = {
                        viewModel.addChallenge(
                            title = newChallengeName,
                            description = newChallengeDescription,
                            duration = newChallengeDuration.toInt(),
                            type = newChallengeType
                        )
                        showDialog = false
                    },
                    selectedType = newChallengeType,
                    onTypeChange = {newChallengeType = it},
                )
            }
        }
    }
}



@Composable
fun CreateChallengeDialog(
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    newChallengeName: String,
    newChallengeDescription: String,
    newChallengeDuration: String,
    selectedType: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onTypeChange: (String) -> Unit // Callback to handle type selection
) {
    val context = LocalContext.current
    val challengeTypes = listOf("Fitness", "Learning", "Productivity", "Creative","Health","Meditation","Other") // Available options
    var expanded by remember { mutableStateOf(false) } // To handle dropdown state

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create a Challenge", color = SkyBlueText) },
        text = {
            Column {
                // Challenge Name
                TextField(
                    value = newChallengeName,
                    onValueChange = onNameChange,
                    label = { Text("Challenge Name", color = GraySecondary) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = SkyBlueText)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Challenge Description
                TextField(
                    value = newChallengeDescription,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description", color = GraySecondary) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = SkyBlueText)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Challenge Duration
                TextField(
                    value = newChallengeDuration,
                    onValueChange = onDurationChange,
                    label = { Text("Duration (days)", color = GraySecondary) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = SkyBlueText)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown for Challenge Type
                Text(
                    text = "Select Challenge Type",
                    color = GraySecondary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Box(modifier = Modifier.fillMaxWidth().wrapContentSize()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            text = selectedType.ifEmpty { "Select Type" },
                            color = GraySecondary
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(200.dp)
                    ) {
                        challengeTypes.forEach { type ->
                            DropdownMenuItem(onClick = {
                                onTypeChange(type) // Update the selected type
                                expanded = false
                            }) {
                                Text(text = type)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newChallengeName.isNotEmpty() && newChallengeDescription.isNotEmpty() && newChallengeDuration.toIntOrNull() ?: 0 > 0 && selectedType.isNotEmpty()) {
                        onCreate()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlueText)
            ) {
                Text("Create", color = GraySecondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SkyBlueText)
            }
        }
    )
}



 @Composable
fun ChallengeCard(
    challenge: Challenge,
    viewModel: ChallengeViewModel,
) {
    val imageId = viewModel.getTypeImage(challenge.type)
    println(challenge.type)
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF00A29A)), // Border color
        backgroundColor = Color(0xFF1A1A1A),// Background color
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            // Semi-transparent centered icon
            Icon(
                painter = painterResource(id= imageId), // Replace with actual icon resource
                 contentDescription = "Challenge Icon",
                tint = Color(0xFF00A29A),// Transparent effect
                modifier=  Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )

            Column {
                Text(text = challenge.title, fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = challenge.description,  fontSize = 16.sp,
                    color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = challenge.progress.toFloat() / challenge.duration.toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (challenge.progress < challenge.duration) {
                                println("progress button clicked")
                                viewModel.updateChallengeProgress(challenge.challengeId, challenge.progress + 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = WhiteText)
                    ) {
                        Text("Log Progress", color = GraySecondary)
                    }

                    IconButton(onClick = {
                        println("delete button clicked")
                        viewModel.deleteChallenge(challengeId = challenge.challengeId)
                        println("delete challenge id is :${challenge.challengeId}")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Challenge",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}