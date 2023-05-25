package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.algebra.surfsafely.adapter.QuestionPagerAdapter
import hr.algebra.surfsafely.databinding.FragmentPlayQuizBinding
import hr.algebra.surfsafely.interfaces.AnswerSelectionListener
import hr.algebra.surfsafely.viewmodel.QuizViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayQuizFragment : Fragment(), AnswerSelectionListener {

    private lateinit var binding: FragmentPlayQuizBinding
    private val quizViewModel by activityViewModel<QuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)

        binding.btnConfirm.setOnClickListener {
            quizViewModel.solveQuiz()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            val adapter = QuestionPagerAdapter(requireContext(), quizViewModel.quiz.value?.questionDtoList!!, this)
            binding.viewPagerQuestions.adapter = adapter
    }

    override fun onAnswerSelected(answerId: Long) {
        quizViewModel.addSelectedAnswer(answerId)
    }

    override fun onAnswerDeselected(answerId: Long) {
        quizViewModel.removeSelectedAnswer(answerId)
    }
}