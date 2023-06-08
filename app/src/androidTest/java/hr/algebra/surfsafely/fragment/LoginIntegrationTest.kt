package hr.algebra.surfsafely.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.activity.AuthenticationActivity
import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.login.LoginResponse
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.service.ApiService
import hr.algebra.surfsafely.viewmodel.AuthenticationUserViewModel
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LoginIntegrationTest {

    private lateinit var viewModel: AuthenticationUserViewModel
    private lateinit var apiService: ApiService
    private lateinit var tokenViewModel: TokenViewModel

    @get:Rule
    val activityRule = ActivityScenarioRule(AuthenticationActivity::class.java)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        apiService = mockk()
        tokenViewModel = mockk()
        viewModel = AuthenticationUserViewModel(apiService, tokenViewModel)
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    @Test
    fun testLoginSuccess() {

        onView(withId(R.id.username_input_login_fragment)).perform(typeText("testUser"))
        onView(withId(R.id.password_input_login_fragment)).perform(
            typeText("password"),
            closeSoftKeyboard()
        )

        viewModel.username.set("testUser")
        viewModel.password.set("password")

        onView(withId(R.id.btnLogin_login_fragment)).perform(click())
        onView(withId(R.id.login_loading_indicator)).check(matches(isDisplayed()))

        val loginDto = LoginRequest("testUser", "password")
        val token = "mockToken"
        val response = Response.success(
            ApiResponse(
                LoginResponse("JWT token generated successfully", token),
                ""
            )
        )
        every { apiService.loginUser(loginDto) } returns mockk {
            every { execute() } returns response
        }
        every { tokenViewModel.updateToken(token) } just Runs
        val result = runBlocking {
            viewModel.loginUser().onSuccess {
                activityRule.scenario.onActivity {
                    it.showToast("Test success")
                }
            }.onFailure {
                activityRule.scenario.onActivity {
                    it.showToast("Test failure")
                }
            }
        }
        assertTrue(result.isSuccess)
    }

    @Test
    fun testLoginFailure() {
        onView(withId(R.id.username_input_login_fragment)).perform(typeText("testUser"))
        onView(withId(R.id.password_input_login_fragment)).perform(
            typeText("password"),
            closeSoftKeyboard()
        )

        viewModel.username.set("testUser")
        viewModel.password.set("password")

        onView(withId(R.id.btnLogin_login_fragment)).perform(click())
        onView(withId(R.id.login_loading_indicator)).check(matches(isDisplayed()))

        val loginDto = LoginRequest("testUser", "password")
        val response = Response.error<ApiResponse<LoginResponse, String>>(
            404,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(LoginResponse(null, null), "Invalid username or password").toString()
            )
        )
        every { apiService.loginUser(loginDto) } returns mockk {
            every { execute() } returns response
        }
        val result = runBlocking {
            viewModel.loginUser().onSuccess {
                activityRule.scenario.onActivity {
                    it.showToast("Test success")
                }
            }.onFailure {
                activityRule.scenario.onActivity {
                    it.showToast("Test failure")
                }
            }
        }

        assertTrue(result.isFailure)
    }
}
