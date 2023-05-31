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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.adapter.QuestionPagerAdapter
import hr.algebra.surfsafely.databinding.FragmentPlayQuizBinding
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.interfaces.AnswerSelectionListener
import hr.algebra.surfsafely.viewmodel.PlayQuizViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayQuizFragment(private val quizId: Long) : Fragment(), AnswerSelectionListener {

    private lateinit var binding: FragmentPlayQuizBinding
    private val playQuizViewModel by viewModel<PlayQuizViewModel>()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        playQuizViewModel.viewModelScope.launch { playQuizViewModel.getQuiz(quizId).onFailure {
            activity?.showToast(it.message.toString())
        } }
        hideBottomNavigationViewAndToolbar()
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            playQuizViewModel.viewModelScope.launch {
                playQuizViewModel.solveQuiz().onSuccess {
                    activity?.showToast(it.toString())
                    parentFragmentManager.popBackStack()
                }.onFailure {
                    activity?.showToast(it.message.toString())
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playQuizViewModel.quiz.observe(viewLifecycleOwner) { quiz ->
            quiz?.let {
                val adapter = QuestionPagerAdapter(requireContext(), quiz.questionDtoList, this)
                binding.viewPagerQuestions.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        showBottomNavigationViewAndToolbar()
        super.onDestroyView()
    }

    override fun onAnswerSelected(answerId: Long?) { playQuizViewModel.addSelectedAnswer(answerId) }

    override fun onAnswerDeselected(answerId: Long?) { playQuizViewModel.removeSelectedAnswer(answerId) }

    private fun hideBottomNavigationViewAndToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun showBottomNavigationViewAndToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }
}