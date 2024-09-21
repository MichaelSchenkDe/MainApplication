package com.product.mainapplication.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.product.mainapplication.HomeViewModel
import com.product.mainapplication.R
import com.product.mainapplication.UiState

@Composable
fun ChatScreen(
    homeViewModel: HomeViewModel,
    onImageSelectedFromHome: (Bitmap?) -> Unit,
    placeholderPrompt: String,
    placeholderResult: String,
    uiState: UiState,
    context: Context,
) {
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }

    // State to hold the selected image (now managed within ChatScreen)
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher for image selection
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImage = context.loadBitmapFromUri(uri)
        }
    }
    // Call the callback when an image is selected from HomeView
    LaunchedEffect(selectedImage) {
        onImageSelectedFromHome(selectedImage)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.baking_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Display selected image if available
            Box(
                modifier = Modifier
                    .weight(1f) // Occupy 50% of the width
                    .then(if (selectedImage != null) Modifier.aspectRatio(1f) else Modifier) // Conditional aspect ratio
            ) {
                selectedImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize() // Fill the entire Box
                            .clip(RectangleShape), // Clip to a rectangle (square in this case)
                        contentScale = ContentScale.Crop // Crop the image to fit the Box
                    )
                }
            }

            // Upload icon
            Box(
                modifier = Modifier
                    .weight(1f) // Occupy 50% of the width
                    .then(if (selectedImage != null) Modifier.aspectRatio(1f) else Modifier)
                    .clickable { launcher.launch("image/*") }
                    .border(BorderStroke(1.dp, Color.Gray)), // Add border here
                contentAlignment = Alignment.Center // Center the icon within the Box
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_upload_48),
                    contentDescription = "Upload Image",
                    modifier = Modifier.size(48.dp), // Adjust the icon size as needed
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Row(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            TextField(
                value = prompt,
                onValueChange = { prompt = it },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                placeholder = { Text(stringResource(R.string.prompt_placeholder)) }
            )
            Button(
                onClick = {
                    selectedImage?.let {
                        homeViewModel.sendPromptWithImage(it, prompt)
                    } ?: run {
                        homeViewModel.sendPrompt(prompt)
                    }
                },
                enabled = prompt.isNotEmpty(), // Enable the button only if there's a prompt
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
                result = uiState.errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = uiState.outputText
            }
            val scrollState = rememberScrollState()
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }
}

// Helper function to load Bitmap from Uri
fun Context.loadBitmapFromUri(uri: Uri): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}
