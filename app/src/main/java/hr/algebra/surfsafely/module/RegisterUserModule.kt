package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.viewmodel.RegisterUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerUserModule = module {
    viewModel { RegisterUserViewModel() }
}