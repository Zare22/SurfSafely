package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.viewmodel.BuyAvatarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val avatarModule = module {
    viewModel { BuyAvatarViewModel(get()) }
}