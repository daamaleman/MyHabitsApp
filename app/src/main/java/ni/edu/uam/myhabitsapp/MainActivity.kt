package ni.edu.uam.myhabitsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ni.edu.uam.myhabitsapp.ui.HabitViewModel
import ni.edu.uam.myhabitsapp.ui.navigation.AppNavigation
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HabitViewModel = viewModel()
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

            HabitFlowTheme(darkTheme = userProfile.darkModeEnabled) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
