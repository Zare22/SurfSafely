package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.dto.quiz.QuestionDto
import hr.algebra.surfsafely.dto.quiz.QuizDto
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateQuizViewModel(private val apiService: ApiService) : ViewModel() {

    private val _questions = MutableLiveData<List<QuestionDto>>(emptyList())
    val questions: LiveData<List<QuestionDto>> = _questions

    suspend fun createQuiz(title: String, description: String, author: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val newQuiz = QuizDto(null, title, description, author, questions.value!!)
            try {
                val response = apiService.createQuiz(newQuiz).execute()
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Quiz couldn't be created!"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error"))
            }
        }
    }

    fun addQuestion(questionDto: QuestionDto) {
        val currentQuestions = _questions.value.orEmpty().toMutableList()
        currentQuestions.add(questionDto)
        _questions.value = currentQuestions
    }
}