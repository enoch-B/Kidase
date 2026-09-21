package com.enoch.kidase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enoch.kidase.audio.AudioPlayerViewModel
import com.enoch.kidase.data.ManifestLoader
import com.enoch.kidase.data.SettingsRepository

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    audioViewModel: AudioPlayerViewModel = viewModel(),
    settingsRepository: SettingsRepository = SettingsRepository(LocalContext.current)
) {
    val context = LocalContext.current
    val currentClipId by audioViewModel.currentClipId.collectAsState()
    val isPlaying by audioViewModel.isPlaying.collectAsState()
    val progress by audioViewModel.playbackProgress.collectAsState()

    val currentClip = remember(currentClipId) {
        if (currentClipId != null) {
            val allClips = ManifestLoader.loadManifest(context)
            allClips.find { it.id == currentClipId }
        } else {
            null
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            Column {
                if (currentClipId != null && currentClip != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(2.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentClip.displayText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { audioViewModel.togglePlayPause() }) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = "Play/Pause",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                IconButton(onClick = { audioViewModel.replayCurrent() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Replay,
                                        contentDescription = "Replay",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
                
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "main",
                        onClick = { navController.navigate("main") { popUpTo("main") { inclusive = false } } },
                        icon = { Icon(Icons.Outlined.Home, contentDescription = "Home", modifier = Modifier.size(20.dp)) },
                        label = { Text("ዋና", style = MaterialTheme.typography.bodyMedium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = MaterialTheme.colorScheme.primary, // to hide the pill
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.tertiary
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == "search",
                        onClick = { navController.navigate("search") { popUpTo("main") { inclusive = false } } },
                        icon = { Icon(Icons.Outlined.Search, contentDescription = "Search", modifier = Modifier.size(20.dp)) },
                        label = { Text("ፈልግ", style = MaterialTheme.typography.bodyMedium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.tertiary
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == "settings",
                        onClick = { navController.navigate("settings") { popUpTo("main") { inclusive = false } } },
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = "Settings", modifier = Modifier.size(20.dp)) },
                        label = { Text("ቅንብሮች", style = MaterialTheme.typography.bodyMedium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = modifier.padding(innerPadding)
        ) {
            composable("main") {
                MainScreen(
                    onNavigateToGroup = { groupId ->
                        navController.navigate("group/$groupId")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
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
            composable("settings") {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModel.Factory(settingsRepository)
                )
                SettingsScreen(
                    viewModel = settingsViewModel
                )
            }
            composable("search") {
                SearchScreen(
                    audioViewModel = audioViewModel
                )
            }
        }
    }
}
