package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.viewmodel.LeaderboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val leaderboardModule = module {
    viewModel { LeaderboardViewModel(get()) }
}