package hr.algebra.surfsafely.dto.quiz

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionDto(
    val id: Long?,
    val questionText: String,
    val answerDtoList: List<AnswerDto>,
    val quizId: Long?
)

