package hr.algebra.surfsafely.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentChangePasswordDialogBinding
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


@RequiresApi(Build.VERSION_CODES.O)
class ChangePasswordDialog : DialogFragment() {

    private lateinit var binding: FragmentChangePasswordDialogBinding
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordDialogBinding.inflate(inflater, container, false)
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            if (binding.newPasswordInput.text.toString() == binding.newPasswordRepeatInput.text.toString()) {
                val changePasswordRequest = ChangePasswordRequest(
                    binding.oldPasswordInput.text.toString(),
                    binding.newPasswordInput.text.toString()
                )
                userViewModel.viewModelScope.launch {
                    userViewModel.changePassword(changePasswordRequest).onSuccess {
                        activity?.showToast(getString(R.string.you_have_changed_your_password_successfully))
                        dismiss()
                    }.onFailure {
                        activity?.showToast(it.message.toString())
                    }
                }
            } else
                activity?.showToast("Your new passwords aren't the same!")
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        }
    }
}