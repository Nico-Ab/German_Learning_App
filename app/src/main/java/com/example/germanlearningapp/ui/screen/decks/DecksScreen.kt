package com.example.germanlearningapp.ui.screen.decks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.germanlearningapp.di.ServiceLocator
import com.example.germanlearningapp.domain.model.Deck
import com.example.germanlearningapp.ui.navigation.Screen

@Composable
fun DecksScreen(
    navController: NavHostController,
    viewModel: DecksViewModel = viewModel(factory = DecksViewModel.provideFactory(
        ServiceLocator.cardRepository
    ))
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Decks",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group by Level
                val grouped = state.decks.groupBy { it.level }
                
                grouped.keys.sorted().forEach { level ->
                    item {
                        Text(
                            text = "Level $level",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(grouped[level] ?: emptyList()) { deck ->
                        DeckItem(deck = deck) {
                            navController.navigate(Screen.Study.createRoute(deck.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeckItem(deck: Deck, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = deck.name,
                    style = MaterialTheme.typography.titleLarge
                )
                if (deck.isPro) {
                    Text(
                        text = "PRO",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = deck.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
