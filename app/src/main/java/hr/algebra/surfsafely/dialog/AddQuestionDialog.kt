package hr.algebra.surfsafely.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentAddQuestionDialogBinding
import hr.algebra.surfsafely.dto.quiz.AnswerDto
import hr.algebra.surfsafely.dto.quiz.QuestionDto
import hr.algebra.surfsafely.interfaces.QuestionDialogListener

class AddQuestionDialog(private val listener: QuestionDialogListener) : DialogFragment() {

    private lateinit var binding: FragmentAddQuestionDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionDialogBinding.inflate(inflater, container, false)
        initButtonClickListeners()

        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnAddAnswer.setOnClickListener { addAnswerView() }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnConfirm.setOnClickListener {

            val questionDto = createQuestionDto()
            listener.onQuestionAdded(questionDto)
            dismiss()
        }
    }

    private fun createQuestionDto(): QuestionDto {
        val questionText = binding.questionTextInput.text.toString()
        val answers = retrieveAnswers()
        return QuestionDto(null, questionText, answers, null)
    }

    private fun retrieveAnswers(): List<AnswerDto> {
        val answerList = mutableListOf<AnswerDto>()
        for (i in 0 until binding.answerContainer.childCount) {
            val answerView = binding.answerContainer.getChildAt(i)
            val answerTextInput = answerView.findViewById<TextInputEditText>(R.id.answer_text_input)
            val answerText = answerTextInput.text.toString()
            val correctAnswerCheckbox = answerView.findViewById<MaterialCheckBox>(R.id.correct_answer_checkbox)
            val isCorrectAnswer = correctAnswerCheckbox.isChecked
            val answerDto = AnswerDto(null, answerText, isCorrectAnswer, null)
            answerList.add(answerDto)
        }
        return answerList
    }

    private fun addAnswerView() {
        val answerView = layoutInflater.inflate(R.layout.item_create_answer, binding.answerContainer, false)
        binding.answerContainer.addView(answerView)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        }
    }
}