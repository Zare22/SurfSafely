package hr.algebra.surfsafely.model

data class Answer(val id: Long, val question: Question, val answerText: String, val isCorrect: Boolean)
