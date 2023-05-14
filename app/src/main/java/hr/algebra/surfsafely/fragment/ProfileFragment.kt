package hr.algebra.surfsafely.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentProfileBinding
import hr.algebra.surfsafely.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUserInfo()

        return binding.root
    }

    private fun setUserInfo() {
        binding.fullName.text = userViewModel.user.value?.firstName.toString()
        binding.email.text = userViewModel.user.value?.email.toString()
    }
}