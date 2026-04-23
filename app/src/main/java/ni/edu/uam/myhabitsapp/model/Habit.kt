package ni.edu.uam.myhabitsapp.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class HabitCategory(val label: String, val emoji: String, val color: Color) {
    HEALTH("Salud", "💚", Color(0xFF64FFAA)),
    STUDY("Estudio", "📚", Color(0xFFA78BFA)),
    FITNESS("Fitness", "🏃", Color(0xFFFB923C)),
    MIND("Mente", "🧘", Color(0xFF60A5FA)),
    WORK("Trabajo", "💼", Color(0xFFFBBF24))
}

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val emoji: String,
    val category: HabitCategory,
    val goalDescription: String,
    val isCompleted: Boolean = false
)

data class WeekDay(
    val shortName: String,
    val isCompleted: Boolean,
    val isToday: Boolean
)

data class UserProfile(
    val name: String = "Ana",
    val email: String = "ana@habitflow.com",
    val password: String = "",
    val imageUri: String? = null,
    val habitsCompleted: Int = 42,
    val currentStreak: Int = 7,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = true
)

