package ni.edu.uam.myhabitsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ni.edu.uam.myhabitsapp.data.UserLocalStorage
import ni.edu.uam.myhabitsapp.ui.HabitViewModel
import ni.edu.uam.myhabitsapp.ui.navigation.AppNavigation
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme

class MainActivity : ComponentActivity() {
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.setDarkModeEnabled(UserLocalStorage.loadDarkModeEnabled(this))

        setContent {
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

            HabitFlowTheme(darkTheme = userProfile.darkModeEnabled) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
