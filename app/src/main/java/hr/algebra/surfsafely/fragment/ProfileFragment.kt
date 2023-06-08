package hr.algebra.surfsafely.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import hr.algebra.surfsafely.databinding.FragmentProfileBinding
import hr.algebra.surfsafely.dialog.ChangePasswordDialog
import hr.algebra.surfsafely.dialog.ChangeProfilePictureDialog
import hr.algebra.surfsafely.dialog.DeleteAccountDialog
import hr.algebra.surfsafely.dialog.EditPersonalInformationDialog
import hr.algebra.surfsafely.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

@RequiresApi(Build.VERSION_CODES.O)
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.userViewModel = userViewModel
        setObservers()
        initButtonClickListeners()
        return binding.root
    }

    private fun setObservers() {
        userViewModel.profileAvatar.observe(viewLifecycleOwner) { image ->
            binding.avatar.setImageBitmap(image.bitmap)
        }
    }

    private fun initButtonClickListeners() {
        binding.btnEditPersonalInfo.setOnClickListener {
            val dialogFragment = EditPersonalInformationDialog()
            dialogFragment.show(parentFragmentManager, "editPersonalInfoDialog")
        }
        binding.btnChangePassword.setOnClickListener {
            val dialogFragment = ChangePasswordDialog()
            dialogFragment.show(parentFragmentManager, "changePasswordDialog")
        }
        binding.btnDeleteAccount.setOnClickListener {
            val dialogFragment = DeleteAccountDialog()
            dialogFragment.show(parentFragmentManager, "deleteAccountDialog")
        }
        binding.btnEditProfilePhoto.setOnClickListener {
            val dialogFragment = ChangeProfilePictureDialog()
            dialogFragment.show(parentFragmentManager, "changeProfilePictureDialog")
        }
    }

}