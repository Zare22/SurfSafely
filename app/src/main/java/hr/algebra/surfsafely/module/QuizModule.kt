package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.viewmodel.PlayQuizViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val quizModule = module {
    viewModel { PlayQuizViewModel(get()) }
}