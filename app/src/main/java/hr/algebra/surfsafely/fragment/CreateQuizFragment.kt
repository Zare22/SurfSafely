package hr.algebra.surfsafely.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textview.MaterialTextView
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


@RequiresApi(Build.VERSION_CODES.O)
class CreateQuizFragment : Fragment(), QuestionDialogListener {

    private lateinit var binding: FragmentCreateQuizBinding
    private val userViewModel by activityViewModel<UserViewModel>()
    private val createQuizViewModel by viewModel<CreateQuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuizBinding.inflate(inflater, container, false)
        initOnClickListeners()
        hideBottomNavigationViewAndToolbar()
        return binding.root
    }

    private fun initOnClickListeners() {
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
                        userViewModel.user.value?.username!!
                    ).onSuccess {
                        activity?.showToast(getString(R.string.you_have_created_your_quiz_successfully))
                    }.onFailure {
                        activity?.showToast(it.message.toString())
                    }
                }
            } else
                activity?.showToast(getString(R.string.please_fill_out_your_form))
        }
    }

    override fun onDestroyView() {
        showBottomNavigationViewAndToolbar()
        super.onDestroyView()
    }

    override fun onQuestionAdded(questionDto: QuestionDto) {
        createQuizViewModel.addQuestion(questionDto)
        addQuestion(questionDto.questionText)
    }

    private fun hideBottomNavigationViewAndToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun showBottomNavigationViewAndToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun addQuestion(questionText: String) {
        val questionView =
            layoutInflater.inflate(R.layout.item_question_simple, binding.questionList, false)
        val textQuestion = questionView.findViewById<MaterialTextView>(R.id.textQuestion)
        textQuestion.text = questionText
        binding.questionList.addView(questionView)
    }
}