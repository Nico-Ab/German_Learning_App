package com.example.germanlearningapp.ui.screen.study

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.germanlearningapp.di.ServiceLocator
import com.example.germanlearningapp.domain.model.Rating
import com.example.germanlearningapp.domain.model.StudyMode
import com.example.germanlearningapp.ui.navigation.Screen

@Composable
fun StudyScreen(
    deckId: Long,
    mode: String = "MIXED", // Passed from navigation
    navController: NavHostController,
    viewModel: StudyViewModel = viewModel(factory = StudyViewModel.provideFactory(
        getNextCardUseCase = ServiceLocator.getNextCardUseCase,
        rateCardUseCase = ServiceLocator.rateCardUseCase
    ))
) {
    val state = viewModel.uiState

    // Load card when screen opens
    LaunchedEffect(deckId, mode) {
        val studyMode = try {
            StudyMode.valueOf(mode)
        } catch (e: IllegalArgumentException) {
            StudyMode.MIXED
        }
        viewModel.loadNextCard(deckId, studyMode)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Exit")
            }
            Text("Study Mode: $mode") 
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.currentCard == null) {
            Text("No more cards to review!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Return to Home")
            }
        } else {
            // Card Content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Show "New" or "Review" label
                    val label = if (state.isNewCard) "New" else "Review"
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.currentCard.frontText,
                            style = MaterialTheme.typography.displayMedium
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))

                        if (state.isAnswerShown) {
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = state.currentCard.backText,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Answer / Rate Controls
            if (!state.isAnswerShown) {
                Button(
                    onClick = { viewModel.showAnswer() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Answer")
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { viewModel.rateCard(Rating.AGAIN) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Again")
                    }
                    Button(
                        onClick = { viewModel.rateCard(Rating.GOOD) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Good")
                    }
                    Button(
                        onClick = { viewModel.rateCard(Rating.EASY) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text("Easy")
                    }
                }
            }
        }
    }
}
