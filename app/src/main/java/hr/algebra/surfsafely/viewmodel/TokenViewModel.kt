package hr.algebra.surfsafely.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.application.SurfSafelyApplication
import hr.algebra.surfsafely.application.SurfSafelyApplication.Companion.getApplication
import hr.algebra.surfsafely.dialog.TokenExpiredDialog
import hr.algebra.surfsafely.framework.isTokenExpired
import hr.algebra.surfsafely.manager.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TokenViewModel(private val tokenManager: TokenManager, private val application: SurfSafelyApplication) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    private val token: LiveData<String?> = _token

    init {
        viewModelScope.launch {
            _token.value = tokenManager.getToken(getApplication()!!.applicationContext)
            scheduleTokenExpirationCheck()
        }
    }

    fun updateToken(token: String) {
        viewModelScope.launch {
            tokenManager.setToken(getApplication()!!.applicationContext, token)
            _token.value = token
        }
    }

    private suspend fun scheduleTokenExpirationCheck() {
        while (true) {
            val token = token.value
            if (token.isNullOrEmpty() || isTokenExpired(token)) {
                val currentActivity = application.currentActivity
                if (currentActivity is FragmentActivity) {
                    val fragmentManager = currentActivity.supportFragmentManager
                    val loginDialog = TokenExpiredDialog()
                    loginDialog.show(fragmentManager, "tokenExpiredDialog")
                }
            }
            delay(TimeUnit.MINUTES.toMillis(1))
        }
    }
}
