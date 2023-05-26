package hr.algebra.surfsafely.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.dto.quiz.QuestionDto
import hr.algebra.surfsafely.dto.quiz.QuizDto
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateQuizViewModel(private val apiService: ApiService) : ViewModel() {


    private val _questions = MutableLiveData<List<QuestionDto>>(emptyList())
    private val questions: LiveData<List<QuestionDto>> = _questions

    fun createQuiz(title: String, description: String, author: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newQuiz = QuizDto(null, title, description, author, questions.value!!)
                val response = apiService.createQuiz(newQuiz).execute()
            }
        }
    }

    fun addQuestion(questionDto: QuestionDto) {
        val currentQuestions = _questions.value.orEmpty().toMutableList()
        currentQuestions.add(questionDto)
        _questions.value = currentQuestions
    }
}