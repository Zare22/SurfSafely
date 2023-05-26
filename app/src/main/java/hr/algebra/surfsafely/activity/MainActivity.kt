package hr.algebra.surfsafely.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.ActivityMainBinding
import hr.algebra.surfsafely.framework.startActivityAndClearStack
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                userViewModel.logout()
                runBlocking {  TokenManager.clearToken(this@MainActivity) }
                this.startActivityAndClearStack<AuthenticationActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initActionBar() { setSupportActionBar(binding.toolbar) }

    private fun initUser() { userViewModel.getUser() }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}