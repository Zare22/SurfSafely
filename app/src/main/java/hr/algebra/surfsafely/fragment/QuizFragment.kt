package hr.algebra.surfsafely.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentQuizBinding
import hr.algebra.surfsafely.framework.replaceFragment

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        binding.testigraj.setOnClickListener {
            this.requireActivity().replaceFragment(R.id.main_fragment_container, PlayQuizFragment(), false)
        }
        return binding.root
    }
}