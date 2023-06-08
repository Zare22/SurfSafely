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
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.framework.showToast
import hr.algebra.surfsafely.service.ApiService
import hr.algebra.surfsafely.viewmodel.AuthenticationUserViewModel
import hr.algebra.surfsafely.viewmodel.TokenViewModel
import io.mockk.every
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
class RegistrationIntegrationTest {

    private lateinit var viewModel: AuthenticationUserViewModel
    private lateinit var apiService: ApiService
    private lateinit var tokenViewModel: TokenViewModel

    private val firstName = "Test"
    private val lastName = "User"
    private val username = "testUser"
    private val password = "password"
    private val email = "testUser@mail.com"

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
    fun registerSuccessTest() {

        onView(withId(R.id.btnCreateAccount)).perform(click())
        onView(withId(R.id.first_name_input)).perform(typeText(firstName), closeSoftKeyboard())
        onView(withId(R.id.last_name_input)).perform(typeText(lastName), closeSoftKeyboard())
        onView(withId(R.id.username_input)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password_input)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.confirm_password_input)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.email_input)).perform(typeText(email), closeSoftKeyboard())

        viewModel.firstName.set(firstName)
        viewModel.lastName.set(lastName)
        viewModel.username.set(username)
        viewModel.password.set(password)
        viewModel.email.set(email)

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.register_loading_indicator)).check(matches(isDisplayed()))

        val userDto = UserDto(firstName, lastName, username, password, email)
        val response = Response.success(ApiResponse(userDto, ""))
        every { apiService.registerUser(userDto) } returns mockk {
            every { execute() } returns response
        }

        val result = runBlocking {
            viewModel.registerUser().onSuccess {
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
    fun registerFailureTest() {

        onView(withId(R.id.btnCreateAccount)).perform(click())
        onView(withId(R.id.first_name_input)).perform(typeText(firstName), closeSoftKeyboard())
        onView(withId(R.id.last_name_input)).perform(typeText(lastName), closeSoftKeyboard())
        onView(withId(R.id.username_input)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password_input)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.confirm_password_input)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.email_input)).perform(typeText(email), closeSoftKeyboard())

        viewModel.firstName.set(firstName)
        viewModel.lastName.set(lastName)
        viewModel.username.set(username)
        viewModel.password.set(password)
        viewModel.email.set(email)

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.register_loading_indicator)).check(matches(isDisplayed()))

        val userDto = UserDto(firstName, lastName, username, password, email)
        val response = Response.error<ApiResponse<UserDto, String>>(
            409,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(userDto, "username already exists").toString()
            )
        )

        val result = runBlocking {
            viewModel.registerUser().onSuccess {
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