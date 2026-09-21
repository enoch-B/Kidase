package com.enoch.kidase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.enoch.kidase.ui.AppNav
import com.enoch.kidase.ui.theme.KidaseTheme
import com.enoch.kidase.data.SettingsRepository
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            val repository = remember { SettingsRepository(context) }
            val fontScale by repository.fontScale.collectAsState(initial = 1.0f)

            KidaseTheme(fontScale = fontScale) {
                AppNav(
                    modifier = Modifier.fillMaxSize(),
                    settingsRepository = repository
                )
            }
        }
    }
}