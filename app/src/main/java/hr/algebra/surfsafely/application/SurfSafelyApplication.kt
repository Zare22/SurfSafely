package hr.algebra.surfsafely.application

import android.app.Application
import hr.algebra.surfsafely.module.serviceModule
import hr.algebra.surfsafely.module.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SurfSafelyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setApplication(this)
        startKoin {
            androidContext(this@SurfSafelyApplication)
            modules(serviceModule, userModule)
        }
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