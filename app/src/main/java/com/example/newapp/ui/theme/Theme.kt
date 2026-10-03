package com.example.newapp.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore extension for preferences
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

/**
 * Enum representing the 3 available color themes for the app.
 */
enum class ThemeType {
    BLUE,
    FOREST_GREEN,
    SUNSET_ORANGE
}

/**
 * Helper object containing ColorSchemes and DataStore management for themes.
 */
object AppThemes {

    // --- Blue Theme (Default) ---
    val LightBlueColorScheme = lightColorScheme(
        primary = BluePrimary,
        onPrimary = BlueOnPrimary,
        primaryContainer = BluePrimaryContainer,
        onPrimaryContainer = BlueOnPrimaryContainer,
        secondary = BlueSecondary,
        onSecondary = BlueOnSecondary,
        secondaryContainer = BlueSecondaryContainer,
        onSecondaryContainer = BlueOnSecondaryContainer,
        background = BlueBackground,
        onBackground = BlueOnBackground,
        surface = BlueSurface,
        onSurface = BlueOnSurface
    )

    val DarkBlueColorScheme = darkColorScheme(
        primary = DarkBluePrimary,
        onPrimary = DarkBlueOnPrimary,
        primaryContainer = DarkBluePrimaryContainer,
        onPrimaryContainer = DarkBlueOnPrimaryContainer,
        secondary = DarkBlueSecondary,
        onSecondary = DarkBlueOnSecondary,
        secondaryContainer = DarkBlueSecondaryContainer,
        onSecondaryContainer = DarkBlueOnSecondaryContainer,
        background = DarkBlueBackground,
        onBackground = DarkBlueOnBackground,
        surface = DarkBlueSurface,
        onSurface = DarkBlueOnSurface
    )

    // --- Forest Green Theme ---
    val LightGreenColorScheme = lightColorScheme(
        primary = GreenPrimary,
        onPrimary = GreenOnPrimary,
        primaryContainer = GreenPrimaryContainer,
        onPrimaryContainer = GreenOnPrimaryContainer,
        secondary = GreenSecondary,
        onSecondary = GreenOnSecondary,
        secondaryContainer = GreenSecondaryContainer,
        onSecondaryContainer = GreenOnSecondaryContainer,
        background = GreenBackground,
        onBackground = GreenOnBackground,
        surface = GreenSurface,
        onSurface = GreenOnSurface
    )

    val DarkGreenColorScheme = darkColorScheme(
        primary = DarkGreenPrimary,
        onPrimary = DarkGreenOnPrimary,
        primaryContainer = DarkGreenPrimaryContainer,
        onPrimaryContainer = DarkGreenOnPrimaryContainer,
        secondary = DarkGreenSecondary,
        onSecondary = DarkGreenOnSecondary,
        secondaryContainer = DarkGreenSecondaryContainer,
        onSecondaryContainer = DarkGreenOnSecondaryContainer,
        background = DarkGreenBackground,
        onBackground = DarkGreenOnBackground,
        surface = DarkGreenSurface,
        onSurface = DarkGreenOnSurface
    )

    // --- Sunset Orange Theme ---
    val LightOrangeColorScheme = lightColorScheme(
        primary = OrangePrimary,
        onPrimary = OrangeOnPrimary,
        primaryContainer = OrangePrimaryContainer,
        onPrimaryContainer = OrangeOnPrimaryContainer,
        secondary = OrangeSecondary,
        onSecondary = OrangeOnSecondary,
        secondaryContainer = OrangeSecondaryContainer,
        onSecondaryContainer = OrangeOnSecondaryContainer,
        background = OrangeBackground,
        onBackground = OrangeOnBackground,
        surface = OrangeSurface,
        onSurface = OrangeOnSurface
    )

    val DarkOrangeColorScheme = darkColorScheme(
        primary = DarkOrangePrimary,
        onPrimary = DarkOrangeOnPrimary,
        primaryContainer = DarkOrangePrimaryContainer,
        onPrimaryContainer = DarkOrangeOnPrimaryContainer,
        secondary = DarkOrangeSecondary,
        onSecondary = DarkOrangeOnSecondary,
        secondaryContainer = DarkOrangeSecondaryContainer,
        onSecondaryContainer = DarkOrangeOnSecondaryContainer,
        background = DarkOrangeBackground,
        onBackground = DarkOrangeOnBackground,
        surface = DarkOrangeSurface,
        onSurface = DarkOrangeOnSurface
    )

    /**
     * Retrieves the appropriate Material3 ColorScheme based on theme type and dark mode flag.
     */
    fun getColorScheme(themeType: ThemeType, isDark: Boolean): ColorScheme {
        return when (themeType) {
            ThemeType.BLUE -> if (isDark) DarkBlueColorScheme else LightBlueColorScheme
            ThemeType.FOREST_GREEN -> if (isDark) DarkGreenColorScheme else LightGreenColorScheme
            ThemeType.SUNSET_ORANGE -> if (isDark) DarkOrangeColorScheme else LightOrangeColorScheme
        }
    }

    // DataStore Preferences Keys
    private val THEME_KEY = stringPreferencesKey("selected_theme")
    private val DARK_MODE_KEY = stringPreferencesKey("is_dark_mode")

    /**
     * Flow to observe the saved theme type. Defaults to BLUE.
     */
    fun getThemeType(context: Context): Flow<ThemeType> {
        return context.dataStore.data.map { preferences ->
            val name = preferences[THEME_KEY] ?: ThemeType.BLUE.name
            try {
                ThemeType.valueOf(name)
            } catch (e: IllegalArgumentException) {
                ThemeType.BLUE
            }
        }
    }

    /**
     * Flow to observe the saved dark mode setting. Returns null if not explicitly set (follows system).
     */
    fun getDarkModePreference(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            preferences[DARK_MODE_KEY]?.toBoolean()
        }
    }

    /**
     * Saves the selected theme type to DataStore.
     */
    suspend fun saveThemeType(context: Context, themeType: ThemeType) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeType.name
        }
    }

    /**
     * Saves the dark mode preference (true, false, or null for system default).
     */
    suspend fun saveDarkModePreference(context: Context, isDark: Boolean?) {
        context.dataStore.edit { preferences ->
            if (isDark == null) {
                preferences.remove(DARK_MODE_KEY)
            } else {
                preferences[DARK_MODE_KEY] = isDark.toString()
            }
        }
    }
}

/**
 * Tydennik App Theme Composable applying the selected theme and dark mode.
 */
@Composable
fun TydennikTheme(
    themeType: ThemeType = ThemeType.BLUE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = AppThemes.getColorScheme(themeType, darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}