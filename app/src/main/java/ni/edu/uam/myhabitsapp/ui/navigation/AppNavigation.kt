package ni.edu.uam.myhabitsapp.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.myhabitsapp.ui.DashboardScreen
import ni.edu.uam.myhabitsapp.ui.HabitViewModel
import ni.edu.uam.myhabitsapp.ui.LoginScreen
import ni.edu.uam.myhabitsapp.ui.PrivacyScreen
import ni.edu.uam.myhabitsapp.ui.ProfileScreen
import ni.edu.uam.myhabitsapp.ui.StatisticsScreen
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Dashboard : Screen("dashboard")
    data object Profile : Screen("profile")
    data object Statistics : Screen("statistics")
    data object Privacy : Screen("privacy")
}

@Composable
fun AppNavigation(viewModel: HabitViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = {
            fadeIn(tween(300)) + slideInHorizontally(tween(300)) { fullWidth -> fullWidth / 4 }
        },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = {
            fadeIn(tween(300)) + slideInHorizontally(tween(300)) { fullWidth -> -fullWidth / 4 }
        },
        popExitTransition = { fadeOut(tween(300)) }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLogin = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStatisticsClick = { navController.navigate(Screen.Statistics.route) },
                onPrivacyClick = { navController.navigate(Screen.Privacy.route) },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Privacy.route) {
            PrivacyScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}



