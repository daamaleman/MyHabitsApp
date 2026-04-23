package ni.edu.uam.myhabitsapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ProfileImageStorage {
    fun saveCircularAvatar(context: Context, sourceUri: Uri): String? {
        val bitmap = runCatching {
            context.contentResolver.openInputStream(sourceUri)?.use(BitmapFactory::decodeStream)
        }.getOrNull() ?: return null

        val circular = bitmap.toCircularBitmap() ?: return null
        val folder = File(context.filesDir, "avatars").apply { mkdirs() }
        val targetFile = File(folder, "avatar_${System.currentTimeMillis()}.png")

        return runCatching {
            FileOutputStream(targetFile).use { stream ->
                circular.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
            Uri.fromFile(targetFile).toString()
        }.getOrNull()
    }

    private fun Bitmap.toCircularBitmap(): Bitmap? {
        if (width <= 0 || height <= 0) return null

        val size = minOf(width, height)
        val left = (width - size) / 2
        val top = (height - size) / 2
        val srcRect = Rect(left, top, left + size, top + size)
        val dstRect = Rect(0, 0, size, size)

        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, srcRect, dstRect, paint)
        paint.xfermode = null

        return output
    }
}

