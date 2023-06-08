package hr.algebra.surfsafely.module

import android.os.Build
import androidx.annotation.RequiresApi
import hr.algebra.surfsafely.viewmodel.CreateQuizViewModel
import hr.algebra.surfsafely.viewmodel.PlayQuizViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val quizModule = module {
    viewModel { PlayQuizViewModel(get()) }
    viewModel { CreateQuizViewModel(get()) }
}