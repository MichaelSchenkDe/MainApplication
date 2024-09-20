package com.product.mainapplication.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.product.mainapplication.R

@Composable
fun SettingsScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture Icon (centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Occupy remaining space
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_account_48), // Replace with your actual profile picture resource
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }

            // Settings Options (Buttons at the bottom)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Handle action for Edit Profile */ },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("Edit Profile", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle action for Privacy */ },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("Privacy", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dark Mode Toggle Button
//                Button(
//                    onClick = { isDarkModeEnabled = !isDarkModeEnabled },
//                    modifier = Modifier.fillMaxWidth(0.8f),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (isDarkModeEnabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
//                    )
//                ) {
//                    Text(
//                        text = if (isDarkModeEnabled) "Disable Dark Mode" else "Enable Dark Mode",
//                        color = if (isDarkModeEnabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                }

//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = { /* Handle action for Logout */ },
//                    modifier = Modifier.fillMaxWidth(0.8f),
//                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
//                ) {
//                    Text("Logout", color = MaterialTheme.colorScheme.onErrorContainer, fontWeight = FontWeight.Bold)
//                }
            }
        }
    }
}