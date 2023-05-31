package hr.algebra.surfsafely.viewmodel

import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.login.LoginResponse
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.service.ApiService
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthenticationUserViewModelTest {


    private lateinit var viewModel: AuthenticationUserViewModel
    private lateinit var apiService: ApiService
    private lateinit var tokenViewModel: TokenViewModel

    @Before
    fun setup() {
        apiService = mockk()
        tokenViewModel = mockk()
        viewModel = AuthenticationUserViewModel(apiService, tokenViewModel)
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `registerUser should return success result when API call is successful`() = runTest {

        // Arrange
        viewModel.firstName.set("John")
        viewModel.lastName.set("Doe")
        viewModel.username.set("johnDoe")
        viewModel.password.set("Password")
        viewModel.email.set("johnDoe@mail.com")

        //Mocking the response
        val userDto = UserDto(
            "John",
            "Doe",
            "johnDoe",
            "Password",
            "johnDoe@mail.com"
        )
        val response = Response.success(ApiResponse(userDto, ""))
        every { apiService.registerUser(userDto) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.registerUser()

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        verify { apiService.registerUser(userDto) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `registerUser should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        viewModel.firstName.set("John")
        viewModel.lastName.set("Doe")
        viewModel.username.set("johnDoe")
        viewModel.password.set("Password")
        viewModel.email.set("johnDoe@mail.com")

        //Mocking the response
        val userDto = UserDto(
            "John",
            "Doe",
            "johnDoe",
            "Password",
            "johnDoe@mail.com"
        )
        val response = Response.error<ApiResponse<UserDto, String>>(
            409,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(userDto, "username already exists").toString()
            )
        )
        every { apiService.registerUser(userDto) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.registerUser()

        // Assert
        assert(result.isFailure)
        assertEquals("Registration failed!", result.exceptionOrNull()?.message)
        verify { apiService.registerUser(userDto) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loginUser should return success result when API call is successful`() = runTest {

        // Arrange
        viewModel.username.set("johnDoe")
        viewModel.password.set("Password")

        val loginDto = LoginRequest("johnDoe", "Password")
        val token = "mockToken"
        val response = Response.success(ApiResponse(LoginResponse("JWT token generated successfully", token), ""))
        every { apiService.loginUser(loginDto) } returns mockk {
            every { execute() } returns response
        }
        every { tokenViewModel.updateToken(token) } just Runs

        // Act
        val result = viewModel.loginUser()

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        verify { apiService.loginUser(loginDto) }
        verify { tokenViewModel.updateToken(token) }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loginUser should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        viewModel.username.set("johnDoe")
        viewModel.password.set("Password")

        val loginDto = LoginRequest("johnDoe", "Password")
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

        // Act
        val result = viewModel.loginUser()

        // Assert
        assert(result.isFailure)
        assertEquals("Failed to log you in!", result.exceptionOrNull()?.message)
        verify { apiService.loginUser(loginDto) }
    }

}