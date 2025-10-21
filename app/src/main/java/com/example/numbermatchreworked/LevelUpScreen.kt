package com.example.numbermatchreworked

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun LevelUpScreen(
    navController: NavController
){
    val level = 2
    Row(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterVertically)
        ){
            Text(
                text = "LEVEL UP!",
                style = typography.headlineMedium,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)

            )
            Text(
                text = "$level",
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier
                .padding(vertical = 100.dp)
            )
        }
        LaunchedEffect(0) {
            delay(2000)
            navController.navigate("game_screen")
        }
    }
}

