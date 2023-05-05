package hr.algebra.surfsafely.model

data class Question(val id: Long, val quiz: Quiz, val questionText: String, val answers: List<Answer>)
