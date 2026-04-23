package ni.edu.uam.myhabitsapp.data

import android.content.Context

private const val PREFS_NAME = "habitflow_user_prefs"
private const val KEY_NAME = "name"
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD = "password"
private const val KEY_IMAGE_URI = "image_uri"

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
}

