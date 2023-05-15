package hr.algebra.surfsafely.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentChangePasswordDialogBinding
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class ChangePasswordDialog : DialogFragment() {

    private lateinit var binding: FragmentChangePasswordDialogBinding
    private val apiService by inject<ApiService>()

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
            lifecycleScope.launch {
                val changePasswordRequest = ChangePasswordRequest(
                    binding.oldPasswordInput.text.toString(),
                    binding.newPasswordInput.text.toString()
                )
                withContext(Dispatchers.IO) {
                    apiService.changePassword(changePasswordRequest).execute()
                }
                dismiss()
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
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        }
    }
}