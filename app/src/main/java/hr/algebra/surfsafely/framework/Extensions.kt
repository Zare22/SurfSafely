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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.auth0.jwt.JWT
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.dto.user.UserImageDto
import java.util.Base64
import java.util.Date
import kotlin.reflect.jvm.internal.impl.resolve.scopes.receivers.ThisClassReceiver

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
fun List<UserImageDto>.toBitmapList() : List<Bitmap> {
    val bitmapList = mutableListOf<Bitmap>()
    val decoder = Base64.getDecoder()
    this.forEach { image ->
        val byteArray = decoder.decode(image.base64)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        bitmapList.add(bitmap)
    }
    return bitmapList
}

@RequiresApi(Build.VERSION_CODES.O)
fun UserImageDto.toBitmap() : Bitmap {
    val decoder = Base64.getDecoder()
    val byteArray = decoder.decode(base64)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}