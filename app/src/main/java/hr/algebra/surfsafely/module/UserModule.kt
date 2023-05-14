package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    viewModel { UserViewModel() }
}