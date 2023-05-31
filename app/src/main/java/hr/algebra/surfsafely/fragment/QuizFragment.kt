package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.adapter.QuizRecycleAdapter
import hr.algebra.surfsafely.databinding.FragmentQuizBinding
import hr.algebra.surfsafely.framework.replaceFragment
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private val apiService by inject<ApiService>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { apiService.getAllQuizzes().execute() }
                if (response.isSuccessful) {
                    val quizList = response.body()?.data
                    withContext(Dispatchers.Main) {
                        val adapter = QuizRecycleAdapter(quizList ?: emptyList()) {
                            this@QuizFragment.requireActivity().replaceFragment(R.id.main_fragment_container, PlayQuizFragment(it), true)
                        }
                        binding.quizRecyclerView.adapter = adapter
                    }
                } else {
                    activity?.showToast("Couldn't fetch our quizzes!")
                }
            } catch (e: Exception) {
                activity?.showToast("Unexpected error!")
            }
        }
        return binding.root
    }
}