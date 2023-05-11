package hr.algebra.surfsafely.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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