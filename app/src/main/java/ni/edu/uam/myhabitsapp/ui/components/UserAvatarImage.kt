package ni.edu.uam.myhabitsapp.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@Composable
fun UserAvatarImage(
    imageUri: String?,
    modifier: Modifier = Modifier,
    placeholderText: String = "👤"
) {
    val context = LocalContext.current
    val bitmap = remember(imageUri) {
        if (imageUri.isNullOrBlank()) {
            null
        } else {
            runCatching {
                context.contentResolver.openInputStream(Uri.parse(imageUri))
                    ?.use(BitmapFactory::decodeStream)
            }.getOrNull()
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = placeholderText,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

