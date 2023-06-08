package hr.algebra.surfsafely.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.auth0.jwt.JWT
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.dto.user.AvatarDto
import hr.algebra.surfsafely.model.AvatarItem
import hr.algebra.surfsafely.model.ProfileAvatar
import java.util.Base64
import java.util.Date

inline fun <reified T : Activity> Context.startActivityAndClearStack() {
    val intent = Intent(this, T::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun Activity.showToast(message: String) {
    runOnUiThread {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}

fun FragmentActivity.replaceFragment(containerId: Int, fragment: Fragment, addToBackStack: Boolean) {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )
        .replace(containerId, fragment)
        .apply {
            if (addToBackStack) addToBackStack(null)
        }
        .commit()
}


fun isTokenExpired(token: String) : Boolean {
    val decodedJWT = JWT.decode(token)
    val expiration = decodedJWT.expiresAt
    return expiration?.before(Date()) ?: true
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<AvatarDto>.toBitmapListWithId() : MutableMap<Long?, Bitmap?> {
    val bitmapMap = mutableMapOf<Long?, Bitmap?>()
    val decoder = Base64.getDecoder()
    this.forEach { image ->
        val base64 = image.photo.substringAfter(",")
        val byteArray = decoder.decode(base64)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        bitmapMap[image.id] = bitmap
    }
    return bitmapMap
}

@RequiresApi(Build.VERSION_CODES.O)
fun AvatarDto.toBitmapWithId() : ProfileAvatar {
    val decoder = Base64.getDecoder()
    val base64 = photo.substringAfter(",")
    val byteArray = decoder.decode(base64)
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    return ProfileAvatar(bitmap, id)
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<AvatarDto>.toAvatarItemListWithPrice() : List<AvatarItem> {
    val avatars = mutableListOf<AvatarItem>()
    val decoder = Base64.getDecoder()
    this.forEach { image ->
        val base64 = image.photo.substringAfter(",")
        val byteArray = decoder.decode(base64)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        avatars.add(AvatarItem(image.id, bitmap, image.price))
    }
    return avatars
}