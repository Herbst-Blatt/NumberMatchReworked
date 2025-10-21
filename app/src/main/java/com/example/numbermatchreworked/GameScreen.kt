package com.example.numbermatchreworked


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun GameScreen(
    navController: NavController,
    gLogic: GameLogic = GameLogic()
) {
    //val points by gLogic.points.observeAsState()
    Column(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxSize(),
    ){

        Row(modifier = Modifier.fillMaxWidth()){
            IconButton(onClick = {
                navController.navigate("home_screen")
            },
                modifier = Modifier
                    .then(Modifier.size(50.dp))
                    .border(1.dp, Color.Black, shape = CircleShape),
            ) {
                Icon(painter = painterResource(R.drawable.home_icon),
                    contentDescription = "Test",
                    modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.weight(1.13f))
            Column(){
                Text(
                    text = "Points",
                    style = typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 18.sp
                )
                Text(
                    text = "0",
                    style =  typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.weight(1f))
            Column(modifier = Modifier.padding(horizontal = 10.dp)){
                Text(
                    text = "Level",
                    style = typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 18.sp
                )
                Text(
                    text = "1",
                    style =  typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        GameCanvas(gLogic)

        Row(modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxSize()
        )
        {
            Spacer(modifier = Modifier.weight(1F))

            IconButton(onClick = {
                gLogic.addMoreButtonClick()
            },
                modifier = Modifier
                    .then(Modifier.size(50.dp))
                    .border(1.dp, Color.Black, shape = CircleShape)

            ) {
                Icon(painter = painterResource(R.drawable.plus_icon),
                    contentDescription = "Test",
                    modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.padding(all = 10.dp))

            IconButton(onClick = {
                navController.navigate("levelup_screen")





            },
                modifier = Modifier
                    .then(Modifier.size(50.dp))
                    .border(1.dp, Color.Black, shape = CircleShape)
            ) {
                Icon(painter = painterResource(R.drawable.tips_icon),
                    contentDescription = "Test",
                    modifier = Modifier.size(30.dp))
            }
        }
    }
}
