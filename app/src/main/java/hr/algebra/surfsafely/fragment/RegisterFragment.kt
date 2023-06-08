package hr.algebra.surfsafely.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.FragmentRegisterBinding
import hr.algebra.surfsafely.framework.replaceFragment
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.viewmodel.AuthenticationUserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerUserViewModel by viewModel<AuthenticationUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        initButtonClickListeners()
        binding.registerUserViewModel = registerUserViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun initButtonClickListeners() {
        binding.btnRegister.setOnClickListener {
            if (isInternetConnected(this.requireContext())) {
                val inputs = listOf(
                    binding.firstNameInput,
                    binding.lastNameInput,
                    binding.usernameInput,
                    binding.passwordInput,
                    binding.emailInput
                )
                if (!inputs.any { it.text.isNullOrBlank() }) {
                    val inputMethodManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                    binding.registerLoadingIndicator.visibility = View.VISIBLE

                    registerUserViewModel.viewModelScope.launch {
                        registerUserViewModel.registerUser().onSuccess {
                            activity?.showToast(getString(R.string.you_have_registered_successfully))
                            activity?.replaceFragment(R.id.authentication_fragment_container, LoginFragment(),false)
                            binding.registerLoadingIndicator.visibility = View.GONE
                        }.onFailure {
                            activity?.showToast(it.message.toString())
                            binding.registerLoadingIndicator.visibility = View.INVISIBLE
                        }
                    }
                } else activity?.showToast(getString(R.string.please_fill_out_your_form))
            } else
                activity?.showToast("There is no internet connection!")
        }
    }

    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}