package com.enoch.kidase.audio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.enoch.kidase.data.ClipEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val player = ExoPlayer.Builder(application).build()
    
    private val _currentClipId = MutableStateFlow<String?>(null)
    val currentClipId: StateFlow<String?> = _currentClipId.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()
    
    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
        })

        // Poll progress while playing to update the progress bar
        viewModelScope.launch {
            while (true) {
                if (player.isPlaying) {
                    val duration = player.duration
                    val position = player.currentPosition
                    if (duration > 0) {
                        _playbackProgress.value = (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
                    }
                }
                delay(100)
            }
        }
    }

    fun playClip(clip: ClipEntry) {
        player.stop()
        player.clearMediaItems()
        
        // Load the new clip's audioFile from assets using the asset URI scheme
        val mediaItem = MediaItem.fromUri("asset:///${clip.audioFile}")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        
        _currentClipId.value = clip.id
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun replayCurrent() {
        if (_currentClipId.value != null) {
            player.seekTo(0)
            player.play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
