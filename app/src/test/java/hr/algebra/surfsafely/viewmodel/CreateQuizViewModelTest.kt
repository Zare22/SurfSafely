package hr.algebra.surfsafely.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.quiz.AnswerDto
import hr.algebra.surfsafely.dto.quiz.QuestionDto
import hr.algebra.surfsafely.dto.quiz.QuizDto
import hr.algebra.surfsafely.service.ApiService
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class CreateQuizViewModelTest {

    private lateinit var viewModel: CreateQuizViewModel
    private lateinit var apiService: ApiService

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        apiService = mockk()
        viewModel = CreateQuizViewModel(apiService)
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    @Test
    fun `createQuiz should return success result when API call is successful`() = runTest {
        // Arrange
        val questionOneAnswers = listOf(
            AnswerDto(1, "Answer 1", true, 1), AnswerDto(2, "Answer 2", false, 1),
            AnswerDto(3, "Answer 3", false, 1), AnswerDto(4, "Answer 4", true, 1),
        )
        val questionTwoAnswers = listOf(
            AnswerDto(5, "Answer 1", true, 2), AnswerDto(6, "Answer 2", false, 2),
            AnswerDto(7, "Answer 3", false, 2), AnswerDto(8, "Answer 4", true, 2),
        )
        val questionThreeAnswers = listOf(
            AnswerDto(9, "Answer 1", true, 3), AnswerDto(10, "Answer 2", false, 3),
            AnswerDto(11, "Answer 3", false, 3), AnswerDto(12, "Answer 4", true, 3),
        )
        val title = "Test Quiz"
        val description = "This is a test quiz"
        val author = "johnDoe"
        val questions = listOf(
            QuestionDto(1, "Question 1", questionOneAnswers, 1),
            QuestionDto(2, "Question 2", questionTwoAnswers, 1),
            QuestionDto(3, "Question 3", questionThreeAnswers, 1)
        )
        val response = Response.success(ApiResponse(QuizDto(null, title, description, author, questions), ""))
        every { apiService.createQuiz(any()) } returns mockk {
            every { execute() } returns response
        }

        viewModel.addQuestion(questions[0])
        viewModel.addQuestion(questions[1])
        viewModel.addQuestion(questions[2])

        // Act
        val result = viewModel.createQuiz(title, description, author)

        // Assert
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())

        // Verify interactions
        verify { apiService.createQuiz(match { it.title == title && it.description == description && it.author == author && it.questionDtoList == questions }) }
    }

    @Test
    fun `createQuiz should return failure result when API call fails`() = runTest {
        // Arrange
        val questionOneAnswers = listOf(
            AnswerDto(1, "Answer 1", true, 1), AnswerDto(2, "Answer 2", false, 1),
            AnswerDto(3, "Answer 3", false, 1), AnswerDto(4, "Answer 4", true, 1),
        )
        val questionTwoAnswers = listOf(
            AnswerDto(5, "Answer 1", true, 2), AnswerDto(6, "Answer 2", false, 2),
            AnswerDto(7, "Answer 3", false, 2), AnswerDto(8, "Answer 4", true, 2),
        )
        val questionThreeAnswers = listOf(
            AnswerDto(9, "Answer 1", true, 3), AnswerDto(10, "Answer 2", false, 3),
            AnswerDto(11, "Answer 3", false, 3), AnswerDto(12, "Answer 4", true, 3),
        )
        val title = "Test Quiz"
        val description = "This is a test quiz"
        val author = "johnDoe"
        val questions = listOf(
            QuestionDto(1, "Question 1", questionOneAnswers, 1),
            QuestionDto(2, "Question 2", questionTwoAnswers, 1),
            QuestionDto(3, "Question 3", questionThreeAnswers, 1)
        )
        val response = Response.error<ApiResponse<QuizDto, String>>(400,
        ResponseBody.create(
            MediaType.parse("application/json"),
            ApiResponse(QuizDto(null, title, description, author, questions), "").toString())
        )


        every { apiService.createQuiz(any()) } returns mockk {
            every { execute() } returns response
        }

        viewModel.addQuestion(questions[0])
        viewModel.addQuestion(questions[1])
        viewModel.addQuestion(questions[2])

        // Act
        val result = viewModel.createQuiz(title, description, author)

        // Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull() is Exception)
    }


    @Test
    fun `addQuestion should add a question to the list of questions`() {
        // Arrange
        val questionOneAnswers = listOf(
            AnswerDto(1, "Answer 1", true, 1), AnswerDto(2, "Answer 2", false, 1),
            AnswerDto(3, "Answer 3", false, 1), AnswerDto(4, "Answer 4", true, 1),
        )
        val question = QuestionDto(1, "Question 1", questionOneAnswers, 1)

        // Act
        viewModel.addQuestion(question)

        // Assert
        val questions = viewModel.questions.value
        assertNotNull(questions)
        assertEquals(1, questions?.size)
        assertEquals(question, questions?.get(0))
    }
}
