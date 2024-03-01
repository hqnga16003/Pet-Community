package com.feature.wigdet

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun TextTile(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
    )
}

@Composable
fun TextSubTitle(title: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = color)
    )
}

@Composable
fun TextSubTitleWithBold(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun TextSubOnboarding(title: String) {
    Text(
        text = title,
        style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.Gray)
    )
}


@Composable
fun MyTextButton(title: String, color: Color, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = title,
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = color)
        )
    }
}