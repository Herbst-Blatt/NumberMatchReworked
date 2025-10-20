package com.example.numbermatchreworked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController



@Composable
fun HomeScreen(navController: NavController){
    Column(
        modifier = Modifier
            .padding(all = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Number Match",
            style = typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Reworked",
            style = typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Image(
            painter = painterResource(R.drawable.placeholder_image),
            "Placeholder",
            modifier = Modifier.size(250.dp))
        Text(
            "Highscore",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = typography.bodyLarge,
        )
        Text(
            "0",
            style = typography.displaySmall,
            fontWeight = FontWeight.Medium
        )

        Button(
            onClick = {
                navController.navigate("game_screen")

            },
            modifier = Modifier.padding(8.dp, 20.dp)) {
            Text(
                "New Game",
                style = typography.bodyLarge
            )
        }
    }
}
