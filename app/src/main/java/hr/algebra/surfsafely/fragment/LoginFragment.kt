package hr.algebra.surfsafely.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.activity.MainActivity
import hr.algebra.surfsafely.databinding.FragmentLoginBinding
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.framework.replaceFragment
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val apiService by inject<ApiService>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setButtonOnClickListeners()
        return binding.root
    }

    private fun setButtonOnClickListeners() {
        binding.btnCreateAccount.setOnClickListener { activity?.replaceFragment(R.id.authentication_fragment_container, RegisterFragment(), true) }

        binding.btnLogin.setOnClickListener {

            lifecycleScope.launch {
                val loginRequest =
                    LoginRequest(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
                val response = withContext(Dispatchers.IO) {
                    apiService.loginUser(loginRequest).execute()
                }
                val token = response.body()?.data?.token
                withContext(Dispatchers.IO) {
                    if (token != null) {
                        TokenManager.setToken((activity as AppCompatActivity).applicationContext, token)
                        (activity as AppCompatActivity).applicationContext.startActivityAndClearStack<MainActivity>()
                    }
                }
            }
        }
    }
}