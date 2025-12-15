package com.example.germanlearningapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.germanlearningapp.di.ServiceLocator
import com.example.germanlearningapp.ui.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.provideFactory(
        ServiceLocator.cardRepository!!
    ))
) {
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()

    // Reload data whenever the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar with Settings
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { navController.navigate(Screen.Settings.route) },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text("⚙️") // Simple Settings Icon placeholder
            }
        }

        // Stats Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Reviews Due: ${state.reviewsDue}")
                Text("New Cards: ${state.newCardsAvailable}")
            }
        }

        Button(
            onClick = { 
                if (state.lastUsedDeck != null) {
                    navController.navigate(Screen.Study.createRoute(state.lastUsedDeck.id))
                } else {
                    navController.navigate(Screen.Decks.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Study Session")
        }

        Button(
            onClick = { navController.navigate(Screen.Decks.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Browse Decks")
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Debug Button
        Button(
            onClick = { 
                scope.launch {
                    viewModel.resetDueDates()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("DEBUG: Reset All Due Dates")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navController.navigate(Screen.Pro.route) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upgrade to Pro")
        }
    }
}
