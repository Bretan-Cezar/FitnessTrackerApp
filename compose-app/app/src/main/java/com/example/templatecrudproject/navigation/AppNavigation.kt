package com.example.templatecrudproject.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.templatecrudproject.entity.viewmodel.EntityViewModel
import com.example.templatecrudproject.screen.create.CreateScreen
import com.example.templatecrudproject.screen.top.TopScreen
import com.example.templatecrudproject.screen.main.MainScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: EntityViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "main") {

        composable(
            route = "main"
        ) {

            MainScreen(
                onCreateFabClick = { navController.navigate("create") },
                onTopFabClick = { viewModel.setTopList(); navController.navigate("top") },
                viewModel = viewModel
            )
        }

        composable(
            route = "create"
        ) {

            CreateScreen(
                onCreateClick = {
                    navController.navigate("main") {
                        popUpTo("create") {inclusive = true}
                    }
                },
                viewModel = viewModel
            )
        }

        composable(
            route = "top"
        ) {

            TopScreen(viewModel)
        }
    }
}