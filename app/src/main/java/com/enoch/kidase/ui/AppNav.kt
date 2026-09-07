package com.enoch.kidase.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enoch.kidase.audio.AudioPlayerViewModel

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    audioViewModel: AudioPlayerViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            MainScreen(
                onNavigateToGroup = { groupId ->
                    navController.navigate("group/$groupId")
                }
            )
        }
        composable("group/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            GroupScreen(
                groupId = groupId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLetter = { letterCode ->
                    navController.navigate("group/$groupId/letter/$letterCode")
                }
            )
        }
        composable("group/{groupId}/letter/{letterCode}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            val letterCode = backStackEntry.arguments?.getString("letterCode")?.toIntOrNull() ?: 0
            
            LetterClipsScreen(
                groupId = groupId,
                letterCodepoint = letterCode,
                audioViewModel = audioViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
