package com.example.routie_wear.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Typography
import com.example.routie_wear.R
import com.example.routie_wear.presentation.theme.Typography

//폰트 불러오기!!
private val ownglyp = FontFamily(
    Font(R.font.ownglyp, weight = FontWeight.Normal)
)

//색상 설정->나중에 추가해주세욥
val appColors = Colors(
    primary = Color(0xD2CBC0),
    background = Color(0xF5F1EB),
    onPrimary = Color(0x453536)
)

//타이포 정하는 코드임당
val appTypography = Typography(
    body1 = TextStyle(
        fontFamily = ownglyp,
        fontSize = 10.sp

    )
)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = appColors,
        typography = Typography,
        content = content
    )
}