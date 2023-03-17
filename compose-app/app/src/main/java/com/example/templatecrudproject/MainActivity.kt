package com.example.templatecrudproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.templatecrudproject.navigation.AppNavigation
import com.example.templatecrudproject.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            ApplicationTheme {

                MainApplication()
            }
        }
    }
}

@Composable
fun MainApplication() {

    AppNavigation()
}
