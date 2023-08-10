package media.uqab.goaltracker.presentation.navigatior

import androidx.compose.runtime.compositionLocalOf

/**
 * Provide navigator in composable functions if provided
 */
val LocalNavigator = compositionLocalOf<ScreenNavigator?> { null }