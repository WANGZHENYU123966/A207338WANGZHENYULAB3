package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 定义整个App的字体风格（Typography）
val Typography = Typography(

    // ===== 正文（Body Text）=====
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal, // 正常字重
        fontSize = 16.sp,               // 字体大小
        letterSpacing = 0.2.sp          // 字间距
    ),

    // ===== 标题（Title）=====
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold, // 半粗
        fontSize = 22.sp,
        letterSpacing = 0.3.sp
    ),

    // ===== 大标题（Headline）=====
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold, // 半粗
        fontSize = 26.sp
    )
)