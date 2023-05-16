package hr.algebra.surfsafely.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.auth0.jwt.JWT
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