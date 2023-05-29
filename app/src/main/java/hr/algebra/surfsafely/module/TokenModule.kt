package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.application.SurfSafelyApplication
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tokenModule = module {
    single { TokenManager }
    viewModel { TokenViewModel(get(), androidApplication() as SurfSafelyApplication) }
}