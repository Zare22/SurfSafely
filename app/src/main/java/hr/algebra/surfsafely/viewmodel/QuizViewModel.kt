package hr.algebra.surfsafely.viewmodel

import android.util.Log
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

class QuizViewModel(private val apiService: ApiService) : ViewModel() {

    private val _quiz = MutableLiveData<QuizDto>()
    val quiz: LiveData<QuizDto> = _quiz

    private val _answers = MutableLiveData<List<Long>>(emptyList())
    val answers: LiveData<List<Long>> = _answers

    init {
        getQuiz()
    }
    fun getQuiz() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = apiService.getQuiz(2).execute()
                _quiz.postValue(response.body()?.data!!)
            }
        }
    }

    fun solveQuiz() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = apiService.solveQuiz(SolveAttemptDto(answers.value!!)).execute()
                Log.d("Rješenje", response.body()?.data?.correctnessPercentage.toString())
            }
        }
    }

    fun addSelectedAnswer(answerId: Long) {
        val currentAnswers = _answers.value.orEmpty().toMutableList()
        currentAnswers.add(answerId)
        _answers.value = currentAnswers
    }

    fun removeSelectedAnswer(answerId: Long) {
        val currentAnswers = _answers.value.orEmpty().toMutableList()
        currentAnswers.remove(answerId)
        _answers.value = currentAnswers
    }

}