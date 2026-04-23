package ni.edu.uam.myhabitsapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

var BackgroundDeep by mutableStateOf(Color(0xFF121212))
var SurfaceCard by mutableStateOf(Color(0xFF1E1E1E))
var SurfaceItem by mutableStateOf(Color(0xFF1A1A1A))
var AccentGreen by mutableStateOf(Color(0xFF64FFAA))
var AccentGreenDark by mutableStateOf(Color(0xFF00C98A))
var AccentPurple by mutableStateOf(Color(0xFFA78BFA))
var AccentOrange by mutableStateOf(Color(0xFFFB923C))
var AccentBlue by mutableStateOf(Color(0xFF60A5FA))
var TextPrimary by mutableStateOf(Color(0xFFE0E0E0))
var TextSecondary by mutableStateOf(Color(0xFF888888))
var TextDisabled by mutableStateOf(Color(0xFF444444))
var BorderSubtle by mutableStateOf(Color(0xFF2A2A2A))
var DangerRed by mutableStateOf(Color(0xFFF87171))

val Purple80 = AccentPurple
val PurpleGrey80 = TextSecondary
val Pink80 = AccentOrange

val Purple40 = AccentGreenDark
val PurpleGrey40 = TextSecondary
val Pink40 = DangerRed
