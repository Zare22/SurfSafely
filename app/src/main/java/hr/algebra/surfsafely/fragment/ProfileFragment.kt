package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.algebra.surfsafely.databinding.FragmentProfileBinding
import hr.algebra.surfsafely.dto.dialog.EditPersonalInformationDialog
import hr.algebra.surfsafely.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setUserInfo()
        initButtonClickListeners()

        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnEditPersonalInfo.setOnClickListener {
            val dialogFragment = EditPersonalInformationDialog()
            dialogFragment.show(parentFragmentManager, "customDialog")
        }
    }

    private fun setUserInfo() {
        binding.fullName.text = userViewModel.user.value?.firstName.plus(" ").plus(userViewModel.user.value?.lastName)
        binding.email.text = userViewModel.user.value?.email
    }
}