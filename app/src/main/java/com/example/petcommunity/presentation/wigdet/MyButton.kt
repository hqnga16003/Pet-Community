package com.example.petcommunity.presentation.wigdet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonAuth(title: String,modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(modifier =  modifier, shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
        )


    }
}