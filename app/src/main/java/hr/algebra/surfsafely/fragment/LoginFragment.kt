package hr.algebra.surfsafely.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.activity.MainActivity
import hr.algebra.surfsafely.databinding.FragmentLoginBinding
import hr.algebra.surfsafely.framework.replaceFragment
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.viewmodel.AuthenticationUserViewModel
import kotlinx.coroutines.launch
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

        binding.btnLoginLoginFragment.setOnClickListener {
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            binding.loginLoadingIndicator.visibility = View.VISIBLE

            val inputs = listOf(binding.usernameInputLoginFragment, binding.passwordInputLoginFragment)
            if (!inputs.any { it.text.isNullOrBlank() }) {
                loginUserViewModel.viewModelScope.launch {
                    loginUserViewModel.loginUser().onSuccess {
                        activity?.showToast(getString(R.string.you_have_logged_in_successfully))
                        (activity as AppCompatActivity).applicationContext.startActivityAndClearStack<MainActivity>()
                        binding.loginLoadingIndicator.visibility = View.GONE
                    }.onFailure {
                        activity?.showToast(it.message.toString())
                        binding.loginLoadingIndicator.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}