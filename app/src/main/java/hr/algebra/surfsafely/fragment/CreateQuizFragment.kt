package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentCreateQuizBinding
import hr.algebra.surfsafely.dialog.AddQuestionDialog
import hr.algebra.surfsafely.dto.quiz.QuestionDto
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.interfaces.QuestionDialogListener
import hr.algebra.surfsafely.viewmodel.CreateQuizViewModel
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateQuizFragment : Fragment(), QuestionDialogListener {

    private lateinit var binding: FragmentCreateQuizBinding
    private val userViewModel by activityViewModel<UserViewModel>()
    private val createQuizViewModel by viewModel<CreateQuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuizBinding.inflate(inflater, container, false)
        binding.addQuestionButton.setOnClickListener {
            val dialogFragment = AddQuestionDialog(this)
            dialogFragment.show(parentFragmentManager, "addQuestionDialog")
        }
        binding.btnCreateQuiz.setOnClickListener {
            val inputs = listOf(binding.titleInput, binding.descriptionInput)
            if (!inputs.any { it.text.isNullOrBlank() }) {
                createQuizViewModel.viewModelScope.launch {
                    createQuizViewModel.createQuiz(
                        binding.titleInput.text.toString(),
                        binding.descriptionInput.text.toString(),
                        userViewModel.user.value!!.username
                    ).onSuccess {
                        activity?.showToast(getString(R.string.you_have_created_your_quiz_successfully))
                    }.onFailure {
                        activity?.showToast(it.message.toString())
                    }
                }
            } else
                activity?.showToast(getString(R.string.please_fill_out_your_form))
        }
        return binding.root
    }

    override fun onQuestionAdded(questionDto: QuestionDto) {
        createQuizViewModel.addQuestion(questionDto)
    }
}