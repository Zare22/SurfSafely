package hr.algebra.surfsafely.dto.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentEditPersonalInformationDialogBinding
import hr.algebra.surfsafely.dto.user.UserUpdateInformationDto
import hr.algebra.surfsafely.service.ApiService
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class EditPersonalInformationDialog : DialogFragment() {

    private lateinit var binding: FragmentEditPersonalInformationDialogBinding
    private val apiService by inject<ApiService>()
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPersonalInformationDialogBinding.inflate(inflater, container, false)
        initFields()
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                val userUpdateInformationDto = UserUpdateInformationDto(
                    binding.emailInput.text.toString(),
                    binding.firstNameInput.text.toString(),
                    binding.lastNameInput.text.toString()
                )
                withContext(Dispatchers.IO) {
                    apiService.updateUserPersonalInformation(userUpdateInformationDto).execute()
                }
                dismiss()
            }
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initFields() {
        binding.firstNameInput.setText(userViewModel.user.value?.firstName)
        binding.lastNameInput.setText(userViewModel.user.value?.lastName)
        binding.emailInput.setText(userViewModel.user.value?.email)
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