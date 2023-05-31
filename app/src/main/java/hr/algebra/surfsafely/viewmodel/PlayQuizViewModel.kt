package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.dto.quiz.QuizDto
import hr.algebra.surfsafely.dto.quiz.SolveAttemptDto
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayQuizViewModel(private val apiService: ApiService) : ViewModel() {

    private val _quiz = MutableLiveData<QuizDto>()
    val quiz: LiveData<QuizDto> = _quiz

    private val _answers = MutableLiveData<List<Long?>>(emptyList())
    private val answers: LiveData<List<Long?>> = _answers

    suspend fun getQuiz(id: Long) : Result<Unit> {
            return withContext(Dispatchers.IO) {
                try {
                    val response = apiService.getQuiz(id).execute()
                    if (response.isSuccessful) {
                        _quiz.postValue(response.body()?.data!!)
                        Result.success(Unit)
                    } else
                        Result.failure(Exception("Couldn't fetch your quiz!"))
                } catch (e: Exception) {
                    Result.failure(Exception("Unexpected error!"))
                }
            }
    }

    suspend fun solveQuiz() : Result<Double> {
            return withContext(Dispatchers.IO) {
                try {
                    val response = apiService.solveQuiz(SolveAttemptDto(answers.value!!)).execute()
                    if (response.isSuccessful) {
                        Result.success(response.body()?.data!!.correctnessPercentage)
                    } else
                        Result.failure(Exception("Error with solving your quiz"))
                } catch (e: Exception) {
                    Result.failure(Exception("Unexpected error"))
                }
            }
    }

    fun addSelectedAnswer(answerId: Long?) {
        val currentAnswers = _answers.value.orEmpty().toMutableList()
        currentAnswers.add(answerId)
        _answers.value = currentAnswers
    }

    fun removeSelectedAnswer(answerId: Long?) {
        val currentAnswers = _answers.value.orEmpty().toMutableList()
        currentAnswers.remove(answerId)
        _answers.value = currentAnswers
    }
}