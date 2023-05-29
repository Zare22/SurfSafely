package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.viewmodel.AuthenticationUserViewModel
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authenticationUserModule = module {
    viewModel { AuthenticationUserViewModel(get(), get()) }
}