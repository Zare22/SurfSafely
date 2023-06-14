package hr.algebra.surfsafely.module

import android.os.Build
import androidx.annotation.RequiresApi
import hr.algebra.surfsafely.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val userModule = module {
    viewModel { UserViewModel(get(), get()) }
}