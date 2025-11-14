package com.example.numbermatchreworked

import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


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

val lineColor = Color(0x66000000)

val correctionSelectionColor = Color(0xFFC9A0DC)

val selectionColor = Color(0xFFC9A0DC)

val wrongSelectionColor = Color(0xFFBA3D25)

val hintColor = Color(0xFF89CFF0)

val buttonColor = ButtonColors(
    containerColor = selectionColor,
    contentColor = Color(0xFF000000),
    disabledContainerColor = Color(0xFF7B7B7D),
    disabledContentColor = Color(0xFF000000)
)