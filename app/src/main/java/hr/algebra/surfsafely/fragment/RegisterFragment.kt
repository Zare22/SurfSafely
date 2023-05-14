package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.databinding.FragmentRegisterBinding
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.module.registerUserModule
import hr.algebra.surfsafely.service.ApiService
import hr.algebra.surfsafely.viewmodel.RegisterUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val apiService by inject<ApiService>()
    private val userDtoViewModel by viewModel<RegisterUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        loadKoinModules(registerUserModule)
        setButtonOnClickListener()
        observeInputFields()
        return binding.root
    }

    private fun observeInputFields() {
        val inputs = listOf(
            binding.firstNameInput,
            binding.lastNameInput,
            binding.usernameInput,
            binding.passwordInput,
            binding.emailInput
        )

        inputs.forEach { input ->
            input.doOnTextChanged { _, _, _, _ ->
                userDtoViewModel.updateUser(
                    binding.firstNameInput.text.toString().trim(),
                    binding.lastNameInput.text.toString().trim(),
                    binding.usernameInput.text.toString().trim(),
                    binding.passwordInput.text.toString().trim(),
                    binding.emailInput.text.toString().trim()
                )
            }
        }
    }

    private fun setButtonOnClickListener() {
        binding.btnRegister.setOnClickListener {

            lifecycleScope.launch {

                val userDto = userDtoViewModel.user.value!!
                val response = withContext(Dispatchers.IO) {
                    apiService.registerUser(userDto).execute()
                }
                withContext(Dispatchers.Main) {
                    if (response.body() != null) activity?.showToast(response.body()!!.data!!.username)
                }
            }


        }
    }
}