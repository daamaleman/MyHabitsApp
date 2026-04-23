package ni.edu.uam.myhabitsapp.ui

import android.content.res.Configuration
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ni.edu.uam.myhabitsapp.data.UserLocalStorage
import ni.edu.uam.myhabitsapp.ui.components.UserAvatarImage
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreen
import ni.edu.uam.myhabitsapp.ui.theme.AccentOrange
import ni.edu.uam.myhabitsapp.ui.theme.AccentPurple
import ni.edu.uam.myhabitsapp.ui.theme.BackgroundDeep
import ni.edu.uam.myhabitsapp.ui.theme.BorderSubtle
import ni.edu.uam.myhabitsapp.ui.theme.DangerRed
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceCard
import ni.edu.uam.myhabitsapp.ui.theme.TextPrimary
import ni.edu.uam.myhabitsapp.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit,
    onStatisticsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val cardShape = RoundedCornerShape(22.dp)
    val subtleBorder = BorderStroke(1.dp, BorderSubtle)
    val layoutDirection = LocalLayoutDirection.current
    val contentMaxWidth = 520.dp
    val horizontalScreenPadding = 24.dp

    Scaffold(containerColor = BackgroundDeep) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDeep),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = contentMaxWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        PaddingValues(
                            start = horizontalScreenPadding + paddingValues.calculateLeftPadding(layoutDirection),
                            top = 18.dp + paddingValues.calculateTopPadding(),
                            end = horizontalScreenPadding + paddingValues.calculateRightPadding(layoutDirection),
                            bottom = 24.dp + paddingValues.calculateBottomPadding()
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceCard,
                        border = subtleBorder
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextPrimary)
                        }
                    }
                    Text(
                        text = "Mi Perfil",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AccentPurple, AccentGreen)))
                            .border(2.dp, AccentGreen.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        UserAvatarImage(
                            imageUri = profile.imageUri,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = profile.name, color = TextPrimary, style = MaterialTheme.typography.headlineLarge)
                    Text(text = profile.email, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Surface(
                        modifier = Modifier.padding(top = 10.dp),
                        color = AccentGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "⭐ Premium",
                            color = AccentGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = profile.habitsCompleted,
                        label = "Completados",
                        shape = cardShape,
                        border = subtleBorder
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = profile.currentStreak,
                        label = "Racha 🔥",
                        shape = cardShape,
                        border = subtleBorder
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = habits.size,
                        label = "Hábitos",
                        shape = cardShape,
                        border = subtleBorder
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))
                Text(text = "Configuración", color = TextPrimary, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = cardShape,
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = subtleBorder
                ) {
                    ListItem(
                        headlineContent = { Text("Notificaciones", color = TextPrimary) },
                        supportingContent = { Text("Recordatorios activos", color = TextSecondary) },
                        leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null, tint = AccentGreen) },
                        trailingContent = {
                            Switch(
                                checked = profile.notificationsEnabled,
                                onCheckedChange = viewModel::setNotificationsEnabled,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = BackgroundDeep,
                                    checkedTrackColor = AccentGreen,
                                    uncheckedThumbColor = Color(0xFF555555),
                                    uncheckedTrackColor = Color(0xFF2A2A2A)
                                )
                            )
                        }
                    )
                    HorizontalDivider(color = BorderSubtle)
                    ListItem(
                        headlineContent = { Text("Modo claro", color = TextPrimary) },
                        supportingContent = { Text("Se mantiene guardado en la app", color = TextSecondary) },
                        leadingContent = { Icon(Icons.Default.WbSunny, contentDescription = null, tint = AccentOrange) },
                        trailingContent = {
                            Switch(
                                checked = !profile.darkModeEnabled,
                                onCheckedChange = { enabled ->
                                    val darkModeEnabled = !enabled
                                    viewModel.setDarkModeEnabled(darkModeEnabled)
                                    UserLocalStorage.saveDarkModeEnabled(context, darkModeEnabled)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = BackgroundDeep,
                                    checkedTrackColor = AccentGreen,
                                    uncheckedThumbColor = Color(0xFF555555),
                                    uncheckedTrackColor = Color(0xFF2A2A2A)
                                )
                            )
                        }
                    )
                    HorizontalDivider(color = BorderSubtle)
                    SettingsChevronItem(
                        icon = Icons.Default.BarChart,
                        iconTint = Color(0xFFFB923C),
                        label = "Estadísticas",
                        description = "Ver historial completo",
                        onClick = onStatisticsClick
                    )
                    HorizontalDivider(color = BorderSubtle)
                    SettingsChevronItem(
                        icon = Icons.Default.Lock,
                        iconTint = Color(0xFF60A5FA),
                        label = "Privacidad",
                        description = "Gestionar datos",
                        onClick = onPrivacyClick
                    )
                    HorizontalDivider(color = BorderSubtle)
                    ListItem(
                        headlineContent = { Text("Cerrar sesión", color = DangerRed) },
                        supportingContent = { Text("Hasta pronto 👋", color = TextSecondary) },
                        leadingContent = { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = DangerRed) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    value: Int,
    label: String,
    shape: RoundedCornerShape,
    border: BorderStroke
) {
    val animatedValue by animateIntAsState(targetValue = value, animationSpec = tween(800), label = "statValue")

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = border
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = animatedValue.toString(), color = AccentGreen, style = MaterialTheme.typography.headlineLarge)
            Text(text = label, color = TextSecondary, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun SettingsChevronItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    description: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(label, color = TextPrimary) },
        supportingContent = { Text(description, color = TextSecondary) },
        leadingContent = { Icon(icon, contentDescription = null, tint = iconTint) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary) }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ProfilePreview() {
    HabitFlowTheme {
        ProfileScreen(
            viewModel = HabitViewModel(),
            onBack = {},
            onStatisticsClick = {},
            onPrivacyClick = {}
        )
    }
}
