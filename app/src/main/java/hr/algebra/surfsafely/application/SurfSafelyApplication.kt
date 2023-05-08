package hr.algebra.surfsafely.application

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.auth0.jwt.JWT
import hr.algebra.surfsafely.manager.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.TimeUnit

class SurfSafelyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setApplication(this)

        val handlerThread = HandlerThread("TokenCheckThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)


        val tokenCheckRunnable = object : Runnable {
            override fun run() {
                val token = runBlocking { TokenManager.getToken(this@SurfSafelyApplication) }
                Log.d("vrijeme", JWT.decode(token!!).expiresAt.toString())
                if (token.isNullOrEmpty() && isTokenExpired(token!!)) {
                    Log.d("prosaovoz", token)
                }

                handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1))
            }
        }

        handler.postDelayed(tokenCheckRunnable, TimeUnit.MINUTES.toMillis(1))
    }

    companion object {
        private var app: Application? = null

        fun setApplication(application: Application) {
            app = application
        }

        fun getApplication(): Application? {
            return app
        }
    }

    fun isTokenExpired(token: String) : Boolean {
        val decodedJWT = JWT.decode(token)
        val expiration = decodedJWT.expiresAt
        return expiration?.before(Date()) ?: true
    }
}