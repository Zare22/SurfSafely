package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.activity.MainActivity
import hr.algebra.surfsafely.databinding.FragmentLoginBinding
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.framework.replaceFragment
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.service.ApiService
import hr.algebra.surfsafely.viewmodel.AuthenticationUserViewModel
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginUserViewModel by viewModel<AuthenticationUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginUserViewModel = loginUserViewModel
        binding.lifecycleOwner = this
        initButtonClickListeners()
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnCreateAccount.setOnClickListener { activity?.replaceFragment(R.id.authentication_fragment_container, RegisterFragment(), true) }

        binding.btnLogin.setOnClickListener {

            val inputs = listOf(binding.usernameInput, binding.passwordInput)

            if (!inputs.any { it.text.isNullOrBlank() }) {
                loginUserViewModel.viewModelScope.launch {
                    loginUserViewModel.loginUser().onSuccess {
                        (activity as AppCompatActivity).applicationContext.startActivityAndClearStack<MainActivity>()
                    }.onFailure {
                        activity?.showToast("Something went wrong with logging in")
                    }
                }
            }
        }
    }
}