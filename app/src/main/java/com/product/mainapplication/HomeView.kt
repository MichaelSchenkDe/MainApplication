package com.product.mainapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.product.mainapplication.ui.views.DashboardScreen
import com.product.mainapplication.ui.views.FavoritesScreen
import com.product.mainapplication.ui.views.HomeScreen
import com.product.mainapplication.ui.views.SettingsScreen
import com.product.mainapplication.ui.views.TaskScreen

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel()
) {
    val selectedImage = remember { mutableIntStateOf(0) }
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        "Home", "Dashboard", "Favorites", "Tasks", "Settings"
    )

    Scaffold(
        contentColor = Color.Blue,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                bottomNavItems.forEachIndexed { _, item ->
                    val iconResourceId = when (item) {
                        "Home" -> R.drawable.ic_home_24
                        "Recipes" -> R.drawable.ic_dashboard_24
                        "Favorites" -> R.drawable.ic_code_24
                        "Tasks" -> R.drawable.ic_schedule_24
                        "Settings" -> R.drawable.ic_account_24
                        else -> R.drawable.ic_person_24
                    }
                    val isSelected = currentRoute(navController) == item
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = iconResourceId),
                                contentDescription = item,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "Home", modifier = Modifier.padding(innerPadding)) {
            composable("Home") {
                HomeScreen(
                    homeViewModel,
                    selectedImage,
                    placeholderPrompt,
                    placeholderResult,
                    uiState,
                    context
                )
            }
            composable("Dashboard") { DashboardScreen() }
            composable("Favorites") { FavoritesScreen() }
            composable("Tasks") { TaskScreen() }
            composable("Settings") { SettingsScreen() }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}