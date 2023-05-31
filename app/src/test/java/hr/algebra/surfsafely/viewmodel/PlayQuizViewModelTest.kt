package hr.algebra.surfsafely.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.quiz.QuizDto
import hr.algebra.surfsafely.dto.quiz.SolveAttemptDto
import hr.algebra.surfsafely.dto.quiz.SolveAttemptResultDto
import hr.algebra.surfsafely.service.ApiService
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class PlayQuizViewModelTest {

    private lateinit var viewModel: PlayQuizViewModel
    private lateinit var apiService: ApiService

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        apiService = mockk()
        viewModel = PlayQuizViewModel(apiService)
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getQuiz should return success result when API call is successful`() = runTest {
        // Arrange
        val quizId = 123L
        val quizDto = QuizDto(quizId, "Quiz Title", "Quiz Description", "Quiz Author", emptyList())
        val response = Response.success(ApiResponse(quizDto, ""))
        every { apiService.getQuiz(quizId) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.getQuiz(quizId)

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
        assertEquals(quizDto, viewModel.quiz.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getQuiz should return failure result when API call fails`() = runTest {
        // Arrange
        val quizId = 123L
        val response = Response.error<ApiResponse<QuizDto, String>>(400, ResponseBody.create(
            MediaType.parse("application/json"),
            ApiResponse(null, null).toString()))
        every { apiService.getQuiz(quizId) } returns mockk {
            every { execute() } returns response
        }

        // Act
        val result = viewModel.getQuiz(quizId)

        // Assert
        assert(result.isFailure)
        assertEquals("Couldn't fetch your quiz!", result.exceptionOrNull()?.message)
        assertNull(viewModel.quiz.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `solveQuiz should return success result with correctness percentage when API call is successful`() = runTest {
        // Arrange
        val answers = listOf(1L, 2L, 3L)
        val correctnessPercentage = 75.0
        val response = Response.success(ApiResponse(SolveAttemptResultDto(correctnessPercentage, 1), ""))
        every { apiService.solveQuiz(SolveAttemptDto(answers)) } returns mockk {
            every { execute() } returns response
        }
        viewModel.addSelectedAnswer(1L)
        viewModel.addSelectedAnswer(2L)
        viewModel.addSelectedAnswer(3L)

        // Act
        val result = viewModel.solveQuiz()

        // Assert
        assert(result.isSuccess)
        assertEquals(correctnessPercentage, result.getOrNull())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `solveQuiz should return failure result when API call fails`() = runTest {
        // Arrange
        val answers = listOf(1L, 2L, 3L)
        val response = Response.error<ApiResponse<SolveAttemptResultDto, String>>(400,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ApiResponse(null, null).toString())
        )
        every { apiService.solveQuiz(SolveAttemptDto(answers)) } returns mockk {
            every { execute() } returns response
        }
        viewModel.addSelectedAnswer(1L)
        viewModel.addSelectedAnswer(2L)
        viewModel.addSelectedAnswer(3L)

        // Act
        val result = viewModel.solveQuiz()

        // Assert
        assert(result.isFailure)
        assertEquals("Error with solving your quiz", result.exceptionOrNull()?.message)
    }

    @Test
    fun `addSelectedAnswer should add answer to the list of selected answers`() {
        // Arrange
        val answerId = 1L

        // Act
        viewModel.addSelectedAnswer(answerId)

        // Assert
        val expectedList = listOf(answerId)
        assertEquals(expectedList, viewModel.answers.value)
    }

    @Test
    fun `removeSelectedAnswer should remove answer from the list of selected answers`() {
        // Arrange
        val answerId = 1L
        viewModel.addSelectedAnswer(1L)
        viewModel.addSelectedAnswer(2L)
        viewModel.addSelectedAnswer(3L)

        // Act
        viewModel.removeSelectedAnswer(answerId)

        // Assert
        val expectedList = listOf(2L, 3L)
        assertEquals(expectedList, viewModel.answers.value)
    }
}
