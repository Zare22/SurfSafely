package hr.algebra.surfsafely.application

import android.app.Application

class SurfSafelyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setApplication(this)
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
}