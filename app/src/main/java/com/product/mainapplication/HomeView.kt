package com.product.mainapplication

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.product.mainapplication.Baking.imageDescriptions
import com.product.mainapplication.Baking.images

val image = arrayOf(
    // Image generated using Gemini from the prompt "cupcake image"
    R.drawable.baked_goods_1,
    // Image generated using Gemini from the prompt "cookies images"
    R.drawable.baked_goods_2,
    // Image generated using Gemini from the prompt "cake images"
    R.drawable.baked_goods_3,
)
val imageDescription = arrayOf(
    R.string.image1_description,
    R.string.image2_description,
    R.string.image3_description,
)

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
        "Home", "Recipes", "Favorites", "Profile", "Settings"
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { _, item ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = item) }, // Replace with appropriate icons
                        label = { Text(item) },
                        selected = false,
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
                HomeContent(
                    homeViewModel,
                    selectedImage,
                    placeholderPrompt,
                    placeholderResult,
                    uiState,
                    context
                )
            }
            composable("Recipes") { RecipesContent() }
            composable("Favorites") { FavoritesContent() }
            composable("Profile") { ProfileContent() }
            composable("Settings") { SettingsContent() }
        }
    }
}

@Composable
fun HomeContent(
    homeViewModel: HomeViewModel,
    selectedImage: MutableIntState,
    placeholderPrompt: String,
    placeholderResult: String,
    uiState: UiState,
    context: Context
) {
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.baking_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(images) { index, image ->
                var imageModifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .requiredSize(200.dp)
                    .clickable {
                        selectedImage.intValue = index
                    }
                if (index == selectedImage.intValue) {
                    imageModifier =
                        imageModifier.border(BorderStroke(4.dp, MaterialTheme.colorScheme.primary))
                }
                Image(
                    painter = painterResource(image),
                    contentDescription = stringResource(imageDescriptions[index]),
                    modifier = imageModifier
                )
            }
        }

        Row(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            TextField(
                value = prompt,
                label = { Text(stringResource(R.string.label_prompt)) },
                onValueChange = { prompt = it },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            Button(
                onClick = {
                    val bitmap = BitmapFactory.decodeResource(
                        context.resources,
                        images[selectedImage.intValue]
                    )
                    homeViewModel.sendPrompt(bitmap, prompt)
                },
                enabled = prompt.isNotEmpty(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(R.string.action_go))
            }
        }

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }
            val scrollState = rememberScrollState()
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }
}

// Placeholder content for other tabs
@Composable
fun RecipesContent() {
    Text("Recipes Content")
}

@Composable
fun FavoritesContent() {
    Text("Favorites Content")
}

@Composable
fun ProfileContent() {
    Text("Profile Content")
}

@Composable
fun SettingsContent() {
    Text("Settings Content")
}