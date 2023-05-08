package hr.algebra.surfsafely.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.surfsafely.client.ApiClient
import hr.algebra.surfsafely.databinding.ActivityLoginBinding
import hr.algebra.surfsafely.dto.login.LoginRequest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}