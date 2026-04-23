package ni.edu.uam.myhabitsapp.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreen
import ni.edu.uam.myhabitsapp.ui.theme.AccentOrange
import ni.edu.uam.myhabitsapp.ui.theme.AccentPurple
import ni.edu.uam.myhabitsapp.ui.theme.BackgroundDeep
import ni.edu.uam.myhabitsapp.ui.theme.BorderSubtle
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceCard
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceItem
import ni.edu.uam.myhabitsapp.ui.theme.TextDisabled
import ni.edu.uam.myhabitsapp.ui.theme.TextPrimary
import ni.edu.uam.myhabitsapp.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val stats by viewModel.statisticsState.collectAsStateWithLifecycle()
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
                    ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                StatisticsHeader(onBack = onBack)

                SummaryGrid(stats = stats)

                WeeklyCard(stats = stats)

                CategoriesCard(stats = stats)
            }
        }
    }
}

@Composable
private fun StatisticsHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextPrimary)
            }
        }

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = "Estadísticas", color = TextPrimary, style = MaterialTheme.typography.titleLarge)
            Text(text = "Resumen minimalista de tu progreso", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SummaryGrid(stats: StatisticsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Hoy",
                value = stats.todayCompletedHabits.toString(),
                subtitle = "completados",
                modifier = Modifier.weight(1f),
                accent = AccentGreen
            )
            SummaryCard(
                title = "Progreso",
                value = "${(stats.completionPercent * 100).roundToInt()}%",
                subtitle = "global",
                modifier = Modifier.weight(1f),
                accent = AccentPurple
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Pendientes",
                value = stats.pendingHabits.toString(),
                subtitle = "por hacer",
                modifier = Modifier.weight(1f),
                accent = AccentOrange
            )
            SummaryCard(
                title = "Racha",
                value = stats.currentStreak.toString(),
                subtitle = "días",
                modifier = Modifier.weight(1f),
                accent = Color(0xFF60A5FA)
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accent: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = TextSecondary, style = MaterialTheme.typography.labelMedium)
            Text(text = value, color = accent, style = MaterialTheme.typography.headlineMedium)
            Text(text = subtitle, color = TextDisabled, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun WeeklyCard(stats: StatisticsUiState) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Semana", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${stats.weekCompletedDays}/7",
                    color = AccentGreen,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                stats.weekDays.forEach { day ->
                    WeekMiniBar(day = day, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { stats.weekCompletionPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(100)),
                color = AccentGreen,
                trackColor = BorderSubtle
            )
        }
    }
}

@Composable
private fun WeekMiniBar(
    day: ni.edu.uam.myhabitsapp.model.WeekDay,
    modifier: Modifier = Modifier
) {
    val barColor by animateColorAsState(
        targetValue = when {
            day.isToday -> AccentGreen
            day.isCompleted -> AccentGreen.copy(alpha = 0.86f)
            else -> Color(0xFF2A2A2A)
        },
        animationSpec = tween(300),
        label = "weekMiniBarColor"
    )
    val labelColor by animateColorAsState(
        targetValue = when {
            day.isToday -> AccentGreen
            day.isCompleted -> TextPrimary
            else -> TextDisabled
        },
        animationSpec = tween(300),
        label = "weekMiniLabelColor"
    )
    val fillHeight by animateFloatAsState(
        targetValue = when {
            day.isToday -> 0.92f
            day.isCompleted -> 0.74f
            else -> 0.34f
        },
        animationSpec = tween(350),
        label = "weekMiniFill"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceItem)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.58f)
                    .height((54.dp * fillHeight).coerceAtLeast(10.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(barColor)
            )
        }
        Text(text = day.shortName, color = labelColor, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun CategoriesCard(stats: StatisticsUiState) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(text = "Categorías", color = TextPrimary, style = MaterialTheme.typography.titleMedium)

            if (stats.categories.isEmpty()) {
                Text(text = "Aún no hay hábitos para mostrar.", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            } else {
                stats.categories.forEach { categoryStats ->
                    CategoryRow(categoryStats = categoryStats)
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(categoryStats: CategoryStats) {
    val fill by animateFloatAsState(
        targetValue = categoryStats.percent,
        animationSpec = tween(500),
        label = "categoryFill"
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${categoryStats.category.emoji} ${categoryStats.category.label}",
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = categoryStats.count.toString(),
                color = categoryStats.category.color,
                style = MaterialTheme.typography.labelMedium
            )
        }

        LinearProgressIndicator(
            progress = { fill },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(100)),
            color = categoryStats.category.color,
            trackColor = BorderSubtle
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun StatisticsPreview() {
    HabitFlowTheme {
        StatisticsScreen(
            viewModel = HabitViewModel(),
            onBack = {}
        )
    }
}

