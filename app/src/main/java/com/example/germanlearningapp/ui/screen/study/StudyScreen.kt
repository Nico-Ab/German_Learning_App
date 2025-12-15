package com.example.germanlearningapp.ui.screen.study

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    mode: String = "MIXED", 
    navController: NavHostController,
    viewModel: StudyViewModel = viewModel(factory = StudyViewModel.provideFactory(
        cardRepository = ServiceLocator.cardRepository!!,
        getNextCardUseCase = ServiceLocator.getNextCardUseCase,
        rateCardUseCase = ServiceLocator.rateCardUseCase
    ))
) {
    val state = viewModel.uiState

    LaunchedEffect(deckId, mode) {
        val studyMode = try {
            StudyMode.valueOf(mode)
        } catch (e: IllegalArgumentException) {
            StudyMode.MIXED
        }
        // Updated to use startSession
        viewModel.startSession(deckId, studyMode)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Exit")
            }
            Text("Study Mode: $mode", style = MaterialTheme.typography.bodyMedium) 
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Top Right Indicator (New/Review)
                    val label = if (state.isNewCard) "New" else "Review"
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    )

                    // Main Content Column
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Front Text always visible
                        Text(
                            text = state.currentCard.frontText,
                            style = MaterialTheme.typography.displayLarge,
                            textAlign = TextAlign.Center
                        )

                        // Back Text only if answer shown
                        if (state.isAnswerShown) {
                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = state.currentCard.backText,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RatingButton(rating = Rating.AGAIN, time = state.nextReviewTimes[Rating.AGAIN] ?: "", onClick = { viewModel.rateCard(Rating.AGAIN) })
                    RatingButton(rating = Rating.GOOD, time = state.nextReviewTimes[Rating.GOOD] ?: "", onClick = { viewModel.rateCard(Rating.GOOD) })
                    RatingButton(rating = Rating.EASY, time = state.nextReviewTimes[Rating.EASY] ?: "", onClick = { viewModel.rateCard(Rating.EASY) })
                }
            }
        }
    }
}

@Composable
private fun RatingButton(rating: Rating, time: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (rating) {
                Rating.AGAIN -> MaterialTheme.colorScheme.error
                Rating.GOOD -> MaterialTheme.colorScheme.primary
                Rating.EASY -> MaterialTheme.colorScheme.tertiary
            }
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = rating.name.lowercase().replaceFirstChar { it.titlecase() })
            Text(text = time, style = MaterialTheme.typography.labelSmall)
        }
    }
}
