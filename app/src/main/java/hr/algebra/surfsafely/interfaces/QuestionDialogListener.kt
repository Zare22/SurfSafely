package hr.algebra.surfsafely.interfaces

import hr.algebra.surfsafely.dto.quiz.QuestionDto

interface QuestionDialogListener {
    fun onQuestionAdded(questionDto: QuestionDto)
}