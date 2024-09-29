package com.product.mainapplication

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.product.mainapplication.ui.views.AddScreen
import com.product.mainapplication.ui.views.ChatScreen
import com.product.mainapplication.ui.views.HomeScreen
import com.product.mainapplication.ui.views.SettingsScreen
import com.product.mainapplication.ui.views.ShopScreen

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel()
) {
    // Wrap the nullable type in a non-null delegate
    var selectedChatImage by remember { mutableStateOf<Bitmap?>(null) }
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        stringResource(R.string.title_home),
        stringResource(R.string.title_shop),
        stringResource(R.string.title_add),
        stringResource(R.string.title_chat),
        stringResource(R.string.title_settings)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                bottomNavItems.forEachIndexed { _, item ->
                    val iconResourceId = when (item) {
                        stringResource(R.string.title_home) -> R.drawable.ic_home_24
                        stringResource(R.string.title_shop) -> R.drawable.ic_cart_24
                        stringResource(R.string.title_add) -> R.drawable.ic_add_circle_24
                        stringResource(R.string.title_chat) -> R.drawable.ic_schedule_24
                        stringResource(R.string.title_settings) -> R.drawable.ic_account_24
                        else -> R.drawable.ic_code_24
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
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.LightGray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = stringResource(R.string.title_home) , modifier = Modifier.padding(innerPadding)) {
            composable("Home") { HomeScreen() }
            composable("Shop") { ShopScreen() }
            composable("Add") { AddScreen(
                homeViewModel,
                onImageSelectedFromHome = { bitmap ->
                    selectedChatImage = bitmap
                },
                "",
                "",
                uiState,
                context) }
            composable("Chat") { ChatScreen() }
            composable("Settings") { SettingsScreen() }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}