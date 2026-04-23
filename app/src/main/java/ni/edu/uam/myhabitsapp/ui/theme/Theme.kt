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
    onPrimary = BackgroundDeep,
    background = Color(0xFFF5F7F7),
    onBackground = Color(0xFF111111),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111111),
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

@Composable
fun HabitFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    HabitFlowTheme(darkTheme = darkTheme, content = content)
}

