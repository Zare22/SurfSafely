package hr.algebra.surfsafely.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.algebra.surfsafely.dto.*
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.dto.user.UserUpdateInformationDto
import hr.algebra.surfsafely.service.ApiService
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class UserViewModelTest {

    private lateinit var viewModel: UserViewModel
    private lateinit var apiService: ApiService
    private lateinit var tokenViewModel: TokenViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        apiService = mockk()
        tokenViewModel = mockk()
        viewModel = UserViewModel(apiService, tokenViewModel)
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUser should return success result when API call is successful`() = runTest {

        val userDto = UserDto("John", "Doe", "johnDoe", "Password", "johnDoe@mail.com")
        val response = Response.success(ApiResponse(userDto, ""))
        every { apiService.getCurrentUser() } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.getUser()

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        assertEquals(userDto, viewModel.user.value)
        verify { apiService.getCurrentUser() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUser should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        val response = Response.error<ApiResponse<UserDto, String>>(
            500,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(UserDto(null, null, null, null, null), "Failed to get user").toString()
            )
        )
        every { apiService.getCurrentUser() } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.getUser()

        // Assert
        assert(result.isFailure)
        assertEquals("Failed to get user", result.exceptionOrNull()?.message)
        assertEquals(null, viewModel.user.value)
        verify { apiService.getCurrentUser() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `changePassword should return success result when API call is successful`() = runTest {

        // Arrange
        val changePasswordRequest = ChangePasswordRequest("oldPassword", "newPassword")
        val response = Response.success(ApiResponse("password was successfully changed", ""))
        every { apiService.changePassword(changePasswordRequest) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.changePassword(changePasswordRequest)

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        verify { apiService.changePassword(changePasswordRequest) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `changePassword should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        val changePasswordRequest = ChangePasswordRequest("oldPassword", "newPassword")
        val response = Response.error<ApiResponse<String, String>>(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(null, null).toString()
            )
        )
        every { apiService.changePassword(changePasswordRequest) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.changePassword(changePasswordRequest)

        // Assert
        assert(result.isFailure)
        assertEquals("Failed to change password", result.exceptionOrNull()?.message)
        verify { apiService.changePassword(changePasswordRequest) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateUserInformation should return success result when API call is successful`() = runTest {

        // Arrange
        val userDto = UserDto(
            "John",
            "Doe",
            "johnDoe",
            "",
            "johnDoe@mail.com"
        )
        val updateInformationDto = UserUpdateInformationDto("johnDoe@mail.com","John", "Doe")
        val response = Response.success(ApiResponse(userDto, ""))
        every { apiService.updateUserPersonalInformation(updateInformationDto) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.updateUserInformation(updateInformationDto)

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        verify { apiService.updateUserPersonalInformation(updateInformationDto) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateUserInformation should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        val updateInformationDto = UserUpdateInformationDto("John", "Doe", "johnDoe@mail.com")
        val response = Response.error<ApiResponse<UserDto, String>>(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(null, null).toString()
            )
        )
        every { apiService.updateUserPersonalInformation(updateInformationDto) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.updateUserInformation(updateInformationDto)

        // Assert
        assert(result.isFailure)
        assertEquals("Failed to update user information", result.exceptionOrNull()?.message)
        verify { apiService.updateUserPersonalInformation(updateInformationDto) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `logout should return success result when API call is successful`() = runTest {

        // Arrange
        val response = Response.success(ApiResponse("token added to blacklist", ""))
        every { apiService.logout() } returns mockk {
            every { execute() } returns response
        }
        every { tokenViewModel.updateToken("") } just Runs

        // Act
        val result = viewModel.logout()

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        verify { apiService.logout() }
        verify { tokenViewModel.updateToken("") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `logout should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        val response = Response.error<ApiResponse<String, String>>(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(null, null).toString()
            )
        )
        every { apiService.logout() } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.logout()

        // Assert
        assert(result.isFailure)
        assertEquals("Failed to logout", result.exceptionOrNull()?.message)
        verify { apiService.logout() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete should return success result when API call is successful`() = runTest {

        // Arrange
        val response = Response.success(ApiResponse("user deleted", ""))
        every { apiService.deleteAccount() } returns mockk {
            every { execute() } returns response
        }
        every { tokenViewModel.updateToken("") } just Runs

        // Act
        val result = viewModel.delete()

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        verify { apiService.deleteAccount() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete should return failure result when API call is unsuccessful`() = runTest {

        // Arrange
        val response = Response.error<ApiResponse<String, String>>(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(null, null).toString()
            )
        )
        every { apiService.deleteAccount() } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.delete()

        // Assert
        assert(result.isFailure)
        assertEquals("Failed to delete account", result.exceptionOrNull()?.message)
        verify { apiService.deleteAccount() }
    }

}
