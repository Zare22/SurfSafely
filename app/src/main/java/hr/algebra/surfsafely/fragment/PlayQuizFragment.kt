package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.adapter.QuestionPagerAdapter
import hr.algebra.surfsafely.databinding.FragmentPlayQuizBinding
import hr.algebra.surfsafely.interfaces.AnswerSelectionListener
import hr.algebra.surfsafely.viewmodel.PlayQuizViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayQuizFragment(private val quizId: Long) : Fragment(), AnswerSelectionListener {

    private lateinit var binding: FragmentPlayQuizBinding
    private val playQuizViewModel by viewModel<PlayQuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        playQuizViewModel.viewModelScope.launch { playQuizViewModel.getQuiz(quizId) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playQuizViewModel.quiz.observe(viewLifecycleOwner) { quiz ->
            quiz?.let {
                val adapter = QuestionPagerAdapter(requireContext(), quiz.questionDtoList, this)
                binding.viewPagerQuestions.adapter = adapter
            }
        }
//            val adapter = QuestionPagerAdapter(requireContext(), playQuizViewModel.quiz.value?.questionDtoList!!, this)
//            binding.viewPagerQuestions.adapter = adapter
    }

    override fun onAnswerSelected(answerId: Long?) {
        playQuizViewModel.addSelectedAnswer(answerId)
    }

    override fun onAnswerDeselected(answerId: Long?) {
        playQuizViewModel.removeSelectedAnswer(answerId)
    }
}