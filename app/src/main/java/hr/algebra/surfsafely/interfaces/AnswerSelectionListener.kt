package hr.algebra.surfsafely.interfaces

interface AnswerSelectionListener {
    fun onAnswerSelected(answerId: Long)
    fun onAnswerDeselected(answerId: Long)
}