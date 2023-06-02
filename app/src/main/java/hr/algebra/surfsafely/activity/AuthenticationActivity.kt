package hr.algebra.surfsafely.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import hr.algebra.surfsafely.databinding.ActivityAuthenticationBinding
import hr.algebra.surfsafely.framework.isTokenExpired
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.module.authenticationUserModule
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private val tokenManager by inject<TokenManager>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        loadKoinModules(authenticationUserModule)
        checkLoggedInState()
        setContentView(binding.root)
    }

    private fun checkLoggedInState() {
        lifecycleScope.launch {
            val token = tokenManager.getToken(this@AuthenticationActivity)
            if (!(token.isNullOrEmpty() || isTokenExpired(token))) {
                this@AuthenticationActivity.startActivityAndClearStack<MainActivity>()
            }
        }
    }
}