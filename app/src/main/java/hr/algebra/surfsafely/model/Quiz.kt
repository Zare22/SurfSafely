package hr.algebra.surfsafely.model

data class Quiz(val id: Long, val title: String, val author: User, val questions: List<Question>)
