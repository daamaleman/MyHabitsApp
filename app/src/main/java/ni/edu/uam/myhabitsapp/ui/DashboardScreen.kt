package ni.edu.uam.myhabitsapp.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ni.edu.uam.myhabitsapp.model.Habit
import ni.edu.uam.myhabitsapp.model.HabitCategory
import ni.edu.uam.myhabitsapp.model.WeekDay
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreen
import ni.edu.uam.myhabitsapp.ui.theme.AccentPurple
import ni.edu.uam.myhabitsapp.ui.theme.BackgroundDeep
import ni.edu.uam.myhabitsapp.ui.theme.BorderSubtle
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceCard
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceItem
import ni.edu.uam.myhabitsapp.ui.theme.TextDisabled
import ni.edu.uam.myhabitsapp.ui.theme.TextPrimary
import ni.edu.uam.myhabitsapp.ui.theme.TextSecondary
import ni.edu.uam.myhabitsapp.ui.components.UserAvatarImage
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: HabitViewModel,
    onProfileClick: () -> Unit
) {
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val weekDays by viewModel.weekDays.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val progress by viewModel.progressPercent.collectAsStateWithLifecycle()

    var showAddSheet by remember { mutableStateOf(false) }
    val contentMaxWidth = 520.dp

    Scaffold(
        containerColor = BackgroundDeep,
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(tween(350)) + fadeIn(tween(350))
            ) {
                FloatingActionButton(
                    onClick = { showAddSheet = true },
                    shape = RoundedCornerShape(18.dp),
                    containerColor = AccentGreen,
                    contentColor = BackgroundDeep,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar hábito",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = contentMaxWidth),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    top = 18.dp,
                    end = 18.dp,
                    bottom = paddingValues.calculateBottomPadding() + 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    DashboardHeader(
                        profileName = profile.name,
                            profileImageUri = profile.imageUri,
                        habits = habits,
                        onProfileClick = onProfileClick
                    )
                }
                item { ProgressCard(progress = progress) }
                item {
                    HabitsSection(
                        habits = habits,
                        onToggle = viewModel::toggleHabit
                    )
                }
                item { WeeklySummary(weekDays = weekDays) }
            }
        }
    }

    if (showAddSheet) {
        AddHabitBottomSheet(
            onDismiss = { showAddSheet = false },
            onSave = { habit ->
                viewModel.addHabit(habit)
                showAddSheet = false
            }
        )
    }
}

@Composable
private fun DashboardHeader(
    profileName: String,
    profileImageUri: String?,
    habits: List<Habit>,
    onProfileClick: () -> Unit
) {
    val dateText = remember {
        SimpleDateFormat("EEEE · dd MMM", Locale.forLanguageTag("es-ES")).format(Date())
            .replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(Locale.forLanguageTag("es-ES")) else char.toString()
            }
    }
    val completed = habits.count { it.isCompleted }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hola, $profileName 👋",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "$dateText · $completed/${habits.size} completados",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        val avatarInteractionSource = remember { MutableInteractionSource() }
        val pressed by avatarInteractionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (pressed) 0.96f else 1f,
            animationSpec = tween(120),
            label = "avatarScale"
        )

        Box(
            modifier = Modifier
                .size(46.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(AccentPurple, AccentGreen)))
                .padding(1.dp)
                .clickable(
                    interactionSource = avatarInteractionSource,
                    indication = null,
                    onClick = onProfileClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(AccentPurple, AccentGreen)))
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            ) {
                UserAvatarImage(
                    imageUri = profileImageUri,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
private fun ProgressCard(progress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1200),
        label = "progressBar"
    )
    val percent = (animatedProgress * 100).roundToInt()

    val motivation = when {
        animatedProgress < 0.30f -> "¡Empieza fuerte!"
        animatedProgress < 0.70f -> "¡Vas muy bien!"
        else -> "¡Estás en racha! 🔥"
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.95f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Progreso de hoy",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "$percent%",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge
                )
                Crossfade(targetState = motivation, label = "motivationText") { message ->
                    Text(
                        text = message,
                        color = AccentGreen,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(100)),
                    color = AccentGreen,
                    trackColor = BorderSubtle
                )
            }

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(74.dp)) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = AccentGreen,
                    trackColor = BorderSubtle,
                    strokeWidth = 6.dp
                )
                Text(
                    text = "$percent%",
                    color = AccentGreen,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun HabitsSection(
    habits: List<Habit>,
    onToggle: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Mis Hábitos", color = Color.White, style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = { }) {
                Text(text = "Ver todos →", color = AccentGreen)
            }
        }

        habits.forEach { habit ->
            HabitItem(
                habit = habit,
                onToggle = { onToggle(habit.id) }
            )
        }
    }
}

@Composable
private fun HabitItem(
    habit: Habit,
    onToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = tween(120),
        label = "habitRowScale"
    )
    val checkColor by animateColorAsState(
        targetValue = if (habit.isCompleted) AccentGreen else Color(0xFF222222),
        animationSpec = tween(250),
        label = "checkColor"
    )
    val nameColor by animateColorAsState(
        targetValue = if (habit.isCompleted) TextDisabled else TextPrimary,
        animationSpec = tween(250),
        label = "nameColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (habit.isCompleted) AccentGreen.copy(alpha = 0.24f) else BorderSubtle.copy(alpha = 0.95f),
        animationSpec = tween(250),
        label = "borderColor"
    )

    Surface(
        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceItem,
        border = BorderStroke(1.dp, borderColor),
        onClick = onToggle,
        interactionSource = interactionSource,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(checkColor),
                contentAlignment = Alignment.Center
            ) {
                if (habit.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = BackgroundDeep,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${habit.name} ${habit.emoji}",
                    color = nameColor,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (habit.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = habit.goalDescription,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            HabitBadge(category = habit.category)
        }
    }
}

@Composable
private fun HabitBadge(category: HabitCategory) {
    Surface(
        color = category.color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, category.color.copy(alpha = 0.22f))
    ) {
        Text(
            text = category.label,
            color = category.color,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun WeeklySummary(weekDays: List<WeekDay>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Esta semana", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Text(text = "🔥 3 racha", color = AccentGreen, style = MaterialTheme.typography.labelMedium)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            weekDays.forEach { day ->
                DayCircle(day = day, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DayCircle(day: WeekDay, modifier: Modifier = Modifier) {
    val background by animateColorAsState(
        targetValue = when {
            day.isToday -> AccentGreen.copy(alpha = 0.08f)
            day.isCompleted -> AccentGreen.copy(alpha = 0.12f)
            else -> Color(0xFF181818)
        },
        animationSpec = tween(400),
        label = "dayBackground"
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            day.isToday -> AccentGreen
            day.isCompleted -> AccentGreen.copy(alpha = 0.25f)
            else -> Color(0xFF222222)
        },
        animationSpec = tween(400),
        label = "dayBorder"
    )
    val textColor by animateColorAsState(
        targetValue = when {
            day.isToday -> AccentGreen
            day.isCompleted -> AccentGreen
            else -> TextDisabled
        },
        animationSpec = tween(400),
        label = "dayText"
    )

    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = CircleShape,
        color = background,
        border = BorderStroke(1.5.dp, borderColor),
        shadowElevation = if (day.isToday) 10.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (day.isCompleted || day.isToday) AccentGreen else TextDisabled)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = day.shortName, color = textColor, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddHabitBottomSheet(
    onDismiss: () -> Unit,
    onSave: (Habit) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("✨") }
    var goal by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(HabitCategory.HEALTH) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceCard,
        dragHandle = {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp).width(44.dp),
                thickness = 4.dp,
                color = BorderSubtle
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nuevo hábito", color = Color.White, style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = SurfaceItem,
                    unfocusedContainerColor = SurfaceItem,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentGreen
                )
            )

            OutlinedTextField(
                value = emoji,
                onValueChange = { emoji = it.take(2) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Emoji") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = SurfaceItem,
                    unfocusedContainerColor = SurfaceItem,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentGreen
                )
            )

            OutlinedTextField(
                value = goal,
                onValueChange = { goal = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Meta") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = SurfaceItem,
                    unfocusedContainerColor = SurfaceItem,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentGreen
                )
            )

            Text(text = "Categoría", color = TextSecondary, style = MaterialTheme.typography.labelMedium)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                HabitCategory.entries.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(text = "${category.emoji} ${category.label}") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            Habit(
                                name = name.trim(),
                                emoji = if (emoji.isBlank()) selectedCategory.emoji else emoji,
                                category = selectedCategory,
                                goalDescription = goal.ifBlank { "Meta personalizada" },
                                isCompleted = false
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen,
                    contentColor = BackgroundDeep
                )
            ) {
                Text(text = "Guardar hábito", fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DashboardPreview() {
    HabitFlowTheme {
        DashboardScreen(
            viewModel = HabitViewModel(),
            onProfileClick = {}
        )
    }
}
