package ni.edu.uam.myhabitsapp.data

import android.content.Context
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Locale

private const val PREFS_NAME = "habitflow_user_prefs"
private const val KEY_NAME = "name"
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD = "password"
private const val KEY_IMAGE_URI = "image_uri"
private const val KEY_LAST_LOGIN_EMAIL = "last_login_email"
private const val KEY_EMAIL_BY_IP_PREFIX = "login_email_ip_"
private const val KEY_DARK_MODE_ENABLED = "dark_mode_enabled"

data class StoredUser(
    val name: String,
    val email: String,
    val password: String,
    val imageUri: String?
)

object UserLocalStorage {
    fun saveUser(context: Context, user: StoredUser) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_NAME, user.name)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_PASSWORD, user.password)
            .putString(KEY_IMAGE_URI, user.imageUri)
            .apply()
    }

    fun loadUser(context: Context): StoredUser? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_NAME, null)?.trim().orEmpty()
        val email = prefs.getString(KEY_EMAIL, null)?.trim().orEmpty()
        val password = prefs.getString(KEY_PASSWORD, null).orEmpty()
        val imageUri = prefs.getString(KEY_IMAGE_URI, null)

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return null
        }

        return StoredUser(
            name = name,
            email = email,
            password = password,
            imageUri = imageUri
        )
    }

    fun saveLoginEmailForCurrentIp(context: Context, email: String) {
        val normalizedEmail = email.trim().lowercase(Locale.getDefault())
        if (normalizedEmail.isBlank()) return

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit().putString(KEY_LAST_LOGIN_EMAIL, normalizedEmail)
        resolveCurrentIpv4Address()?.let { ip ->
            editor.putString("$KEY_EMAIL_BY_IP_PREFIX$ip", normalizedEmail)
        }
        editor.apply()
    }

    fun loadLoginEmailForCurrentIp(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ipEmail = resolveCurrentIpv4Address()?.let { ip ->
            prefs.getString("$KEY_EMAIL_BY_IP_PREFIX$ip", null)
        }

        return ipEmail?.trim().orEmpty().ifBlank {
            prefs.getString(KEY_LAST_LOGIN_EMAIL, null)?.trim().orEmpty()
        }
    }

    fun saveDarkModeEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_MODE_ENABLED, enabled)
            .apply()
    }

    fun loadDarkModeEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE_ENABLED, true)
    }

    private fun resolveCurrentIpv4Address(): String? {
        return runCatching {
            NetworkInterface.getNetworkInterfaces().toList()
                .asSequence()
                .flatMap { networkInterface -> networkInterface.inetAddresses.toList().asSequence() }
                .firstOrNull { address ->
                    !address.isLoopbackAddress && address is Inet4Address
                }
                ?.hostAddress
        }.getOrNull()
    }
}
