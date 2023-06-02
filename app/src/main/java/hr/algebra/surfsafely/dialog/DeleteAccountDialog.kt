package hr.algebra.surfsafely.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.activity.AuthenticationActivity
import hr.algebra.surfsafely.databinding.FragmentDeleteAccountDialogBinding
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DeleteAccountDialog : DialogFragment() {

    private lateinit var binding: FragmentDeleteAccountDialogBinding
    private val userViewModel by activityViewModel<UserViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteAccountDialogBinding.inflate(inflater, container, false)
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            if (binding.usernameInput.text.toString() == userViewModel.user.value?.username) {
                userViewModel.viewModelScope.launch {
                    userViewModel.delete().onSuccess {
                        activity?.showToast(getString(R.string.you_have_successfully_delete_your_account))
                        dismiss()
                        requireActivity().startActivityAndClearStack<AuthenticationActivity>()
                    }.onFailure {
                        activity?.showToast(it.message.toString())
                    }

                }
            }
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