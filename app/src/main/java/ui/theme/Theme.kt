package ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColors = lightColorScheme(// 定义浅色主题（Light Mode 的颜色方案）
    primary = GreenPrimary,   // 主颜色（按钮、重点UI）
    onPrimary = GreenOnPrimary, // 主色上的文字颜色
    primaryContainer = GreenPrimaryContainer, // 主色浅背景（卡片）
    onPrimaryContainer = GreenOnPrimaryContainer, // 卡片上的文字

    secondary = GreenSecondary,
    onSecondary = GreenOnSecondary,
    secondaryContainer = GreenSecondaryContainer,
    onSecondaryContainer = GreenOnSecondaryContainer,

    tertiary = GreenTertiary,
    onTertiary = GreenOnTertiary,
    tertiaryContainer = GreenTertiaryContainer,
    onTertiaryContainer = GreenOnTertiaryContainer,

    background = AppBackground,
    onBackground = OnAppBackground,

    surface = AppSurface,
    onSurface = OnAppSurface,

    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = OnAppSurfaceVariant,

    outline = AppOutline,
    outlineVariant = AppOutlineVariant
)

@Composable
fun HabitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}

