package com.example.numbermatchreworked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Spacer(modifier = Modifier.padding(25.dp))

        Image(
            painter = painterResource(R.drawable.homescreen_image),
            "Placeholder",
            modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.padding(40.dp))

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

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            onClick = {
                navController.navigate("game_screen")
            },
            colors = buttonColor,
            modifier = Modifier.padding(8.dp, 20.dp)) {
            Text(
                "New Game",
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Button(
            onClick = {
                //navController.navigate("game_screen")
            },
            colors = buttonColor,
            modifier = Modifier.padding(8.dp, 0.dp)) {
            Text(
                "Continue Game",
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
