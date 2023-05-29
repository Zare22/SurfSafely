package hr.algebra.surfsafely.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentTokenExpiredDialogBinding
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TokenExpiredDialog : DialogFragment() {

    private lateinit var binding: FragmentTokenExpiredDialogBinding
    private val tokenViewModel by activityViewModel<TokenViewModel>()
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTokenExpiredDialogBinding.inflate(inflater, container, false)
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            userViewModel.viewModelScope.launch {
                userViewModel.loginUser(binding.passwordInput.text.toString()).onSuccess {
                    dismiss()
                }.onFailure {
                    activity?.showToast("Check your password")
                }
            }
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