package hr.algebra.surfsafely.dto.quiz

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizDto(
    val id: Long,
    val title: String,
    val author: String,
    val questionDtoList: List<QuestionDto>
)