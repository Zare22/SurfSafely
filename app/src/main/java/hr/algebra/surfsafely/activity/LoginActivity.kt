package hr.algebra.surfsafely.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hr.algebra.surfsafely.client.ApiClient
import hr.algebra.surfsafely.databinding.ActivityLoginBinding
import hr.algebra.surfsafely.dto.UserDto
import hr.algebra.surfsafely.dto.checkurl.CheckUrlRequest
import hr.algebra.surfsafely.dto.checkurl.ThreatEntry
import hr.algebra.surfsafely.dto.login.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}