package hr.algebra.surfsafely.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import hr.algebra.surfsafely.module.quizModule
import hr.algebra.surfsafely.module.serviceModule
import hr.algebra.surfsafely.module.tokenModule
import hr.algebra.surfsafely.module.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SurfSafelyApplication : Application(), Application.ActivityLifecycleCallbacks {
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        setApplication(this)
        startKoin {
            androidContext(this@SurfSafelyApplication)
            modules(serviceModule, userModule, quizModule, tokenModule)
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

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

}