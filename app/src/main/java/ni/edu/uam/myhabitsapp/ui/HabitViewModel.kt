package ni.edu.uam.myhabitsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ni.edu.uam.myhabitsapp.model.Habit
import ni.edu.uam.myhabitsapp.model.HabitCategory
import ni.edu.uam.myhabitsapp.model.UserProfile
import ni.edu.uam.myhabitsapp.model.WeekDay

class HabitViewModel : ViewModel() {

    private val _habits = MutableStateFlow(sampleHabits())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    private val _weekDays = MutableStateFlow(sampleWeekDays())
    val weekDays: StateFlow<List<WeekDay>> = _weekDays.asStateFlow()

    private val _userProfile = MutableStateFlow(initialProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    val progressPercent: StateFlow<Float> = habits
        .map { list ->
            if (list.isEmpty()) 0f else list.count { it.isCompleted }.toFloat() / list.size.toFloat()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    fun toggleHabit(habitId: String) {
        _habits.update { list ->
            list.map { habit ->
                if (habit.id == habitId) habit.copy(isCompleted = !habit.isCompleted) else habit
            }
        }
        syncProfileCompletedCount()
    }

    fun addHabit(habit: Habit) {
        _habits.update { current -> current + habit }
        syncProfileCompletedCount()
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _userProfile.update { it.copy(notificationsEnabled = enabled) }
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        _userProfile.update { it.copy(darkModeEnabled = enabled) }
    }

    fun applyRegisteredUser(
        name: String,
        email: String,
        password: String,
        imageUri: String?
    ) {
        _userProfile.update {
            it.copy(
                name = name,
                email = email,
                password = password,
                imageUri = imageUri
            )
        }
    }

    fun updateProfileImage(imageUri: String?) {
        _userProfile.update { it.copy(imageUri = imageUri) }
    }

    private fun syncProfileCompletedCount() {
        val completed = _habits.value.count { it.isCompleted }
        _userProfile.update { it.copy(habitsCompleted = completed) }
    }

    private fun initialProfile() = UserProfile(
        name = "Ana",
        email = "ana@habitflow.com",
        password = "",
        imageUri = null,
        habitsCompleted = sampleHabits().count { it.isCompleted },
        currentStreak = 7,
        notificationsEnabled = true,
        darkModeEnabled = true
    )

    private fun sampleHabits() = listOf(
        Habit("1", "Beber agua", "💧", HabitCategory.HEALTH, "2L", true),
        Habit("2", "Leer", "📚", HabitCategory.STUDY, "30 min · 08:00 AM", true),
        Habit("3", "Meditar", "🧘", HabitCategory.MIND, "10 min · 07:00 PM", false),
        Habit("4", "Ejercicio", "🏃", HabitCategory.FITNESS, "45 min · 06:00 AM", true)
    )

    private fun sampleWeekDays() = listOf(
        WeekDay("L", true, false),
        WeekDay("M", true, false),
        WeekDay("X", true, false),
        WeekDay("J", false, true),
        WeekDay("V", false, false),
        WeekDay("S", false, false),
        WeekDay("D", false, false)
    )
}


