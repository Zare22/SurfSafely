package hr.algebra.surfsafely.framework

import android.app.Activity
import android.widget.Toast

fun Activity.showToast(message: String) {
    runOnUiThread {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}