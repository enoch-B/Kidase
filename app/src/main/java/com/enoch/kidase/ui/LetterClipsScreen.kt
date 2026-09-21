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
