package hr.algebra.surfsafely.activity

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.ActivityMainBinding
import hr.algebra.surfsafely.fragment.LeaderboardFragment
import hr.algebra.surfsafely.framework.replaceFragment
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        initUser()
        initNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                userViewModel.viewModelScope.launch {
                    userViewModel.logout().onSuccess {
                        delay(500)
                        this@MainActivity.showToast("You have successfully logged out!")
                        this@MainActivity.startActivityAndClearStack<AuthenticationActivity>()
                    }.onFailure {
                        this@MainActivity.showToast(it.message.toString())
                    }
                }
                true
            }
            R.id.show_leaderboard -> {
                this@MainActivity.replaceFragment(R.id.main_fragment_container, LeaderboardFragment(), true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initUser() {
        userViewModel.viewModelScope.launch {
            userViewModel.getUser().onSuccess {}
                .onFailure {
                    this@MainActivity.showToast(it.message.toString())
                }
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}