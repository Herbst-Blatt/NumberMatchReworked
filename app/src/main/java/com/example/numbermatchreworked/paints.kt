package com.example.numbermatchreworked

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt


var fontSize = 22.sp

val defaultTextStyle = TextStyle(
    fontSize = fontSize,
    color = Color.Black,
    fontWeight = FontWeight.Bold
)

val solvedTextStyle = TextStyle(
    fontSize = fontSize,
    color = Color.LightGray
)

val wrongTextStyle = TextStyle(
    fontSize = fontSize,
    color = Color(0xFFBA3D25)
)

val correctTextStyle = TextStyle(
    fontSize = fontSize,
    color = Color(0xFF84DB4D),
    fontWeight = FontWeight.Bold
)

val selectionColor = Color(0xFFC9A0DC)

val correctionSelectionColor = Color(0xFF84DB4D)

val wrongSelectionColor = Color(0xFFBA3D25)

val hintColor = Color(0xFF89CFF0)