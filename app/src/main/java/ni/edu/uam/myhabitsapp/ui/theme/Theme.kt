package ni.edu.uam.myhabitsapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val HabitFlowDarkScheme = darkColorScheme(
    primary = AccentGreen,
    onPrimary = BackgroundDeep,
    background = BackgroundDeep,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    secondary = AccentPurple,
    tertiary = AccentOrange,
    outline = BorderSubtle,
    error = DangerRed
)

private val HabitFlowLightScheme = lightColorScheme(
    primary = AccentGreenDark,
    onPrimary = Color.White,
    background = Color(0xFFF4F7F9),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    secondary = AccentPurple,
    tertiary = AccentOrange,
    outline = Color(0xFFE2E8F0),
    error = DangerRed
)

private val HabitFlowShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

private fun applyHabitFlowPalette(darkTheme: Boolean) {
    if (darkTheme) {
        BackgroundDeep = Color(0xFF121212)
        SurfaceCard = Color(0xFF1E1E1E)
        SurfaceItem = Color(0xFF1A1A1A)
        AccentGreen = Color(0xFF64FFAA)
        AccentGreenDark = Color(0xFF00C98A)
        AccentPurple = Color(0xFFA78BFA)
        AccentOrange = Color(0xFFFB923C)
        AccentBlue = Color(0xFF60A5FA)
        TextPrimary = Color(0xFFE0E0E0)
        TextSecondary = Color(0xFF888888)
        TextDisabled = Color(0xFF444444)
        BorderSubtle = Color(0xFF2A2A2A)
        DangerRed = Color(0xFFF87171)
    } else {
        BackgroundDeep = Color(0xFFF4F7F9)
        SurfaceCard = Color(0xFFFFFFFF)
        SurfaceItem = Color(0xFFF1F5F9)
        AccentGreen = Color(0xFF34C98A)
        AccentGreenDark = Color(0xFF1BAE74)
        AccentPurple = Color(0xFF8B5CF6)
        AccentOrange = Color(0xFFF97316)
        AccentBlue = Color(0xFF3B82F6)
        TextPrimary = Color(0xFF0F172A)
        TextSecondary = Color(0xFF64748B)
        TextDisabled = Color(0xFF94A3B8)
        BorderSubtle = Color(0xFFE2E8F0)
        DangerRed = Color(0xFFEF4444)
    }
}

@Composable
fun HabitFlowTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    applyHabitFlowPalette(darkTheme)
    val colorScheme = if (darkTheme) HabitFlowDarkScheme else HabitFlowLightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = habitFlowTypography(),
        shapes = HabitFlowShapes,
        content = content
    )
}

@Composable
fun MyHabitsAppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    HabitFlowTheme(darkTheme = darkTheme, content = content)
}
