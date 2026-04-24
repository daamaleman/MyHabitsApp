package ni.edu.uam.myhabitsapp.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeLong(value.value.toLong())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeLong().toULong())
    }
}

@Serializable
data class HabitCategory(
    val label: String,
    val emoji: String,
    @Serializable(with = ColorSerializer::class) val color: Color
) {
    companion object {
        val HEALTH = HabitCategory("Salud", "💚", Color(0xFF64FFAA))
        val STUDY = HabitCategory("Estudio", "📚", Color(0xFFA78BFA))
        val FITNESS = HabitCategory("Fitness", "🏃", Color(0xFFFB923C))
        val MIND = HabitCategory("Mente", "🧘", Color(0xFF60A5FA))
        val WORK = HabitCategory("Trabajo", "💼", Color(0xFFFBBF24))

        fun defaultCategories() = listOf(HEALTH, STUDY, FITNESS, MIND, WORK)
    }
}

@Serializable
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
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
