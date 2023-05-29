package hr.algebra.surfsafely.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.surfsafely.databinding.ActivityAuthenticationBinding
import hr.algebra.surfsafely.module.authenticationUserModule
import org.koin.core.context.loadKoinModules

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        loadKoinModules(authenticationUserModule)
        setContentView(binding.root)
    }
}