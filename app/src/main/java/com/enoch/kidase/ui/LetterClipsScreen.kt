package com.enoch.kidase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.enoch.kidase.audio.AudioPlayerViewModel
import com.enoch.kidase.data.ManifestLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LetterClipsScreen(
    groupId: String,
    letterCodepoint: Int,
    audioViewModel: AudioPlayerViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val letterChar = letterCodepoint.toChar()

    val clipsForLetter = remember(groupId, letterCodepoint) {
        val allClips = ManifestLoader.loadManifest(context)
        val groupClipsMap = ManifestLoader.groupClips(allClips)
        val sortedGroup = groupClipsMap[groupId] ?: emptyList()
        sortedGroup.find { it.first == letterChar }?.second ?: emptyList()
    }

    val currentClipId by audioViewModel.currentClipId.collectAsState()
    val isPlaying by audioViewModel.isPlaying.collectAsState()
    val progress by audioViewModel.playbackProgress.collectAsState()

    val currentClip = remember(currentClipId, clipsForLetter) {
        clipsForLetter.find { it.id == currentClipId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = letterChar.toString()) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            )
        },
        bottomBar = {
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(clipsForLetter) { clip ->
                GroupCard(
                    groupName = clip.displayText,
                    isHighlighted = clip.id == currentClipId,
                    onClick = { audioViewModel.playClip(clip) }
                )
            }
        }
    }
}
