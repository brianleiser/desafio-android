package com.picpay.desafio.android

import app.cash.turbine.test
import com.picpay.desafio.android.domain.Result
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsers
import com.picpay.desafio.android.presentation.UserListViewModel
import com.picpay.desafio.android.presentation.state.ViewEvent
import com.picpay.desafio.android.presentation.state.ViewState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ViewModelTest {

    private val dispatcher = TestCoroutineDispatcher().apply { Dispatchers.setMain(this) }
    private val getUsers = mockk<GetUsers>()
    private val viewModel = UserListViewModel(getUsers)

    private val initialState = ViewState()

    private val usersList = listOf(
        User(1, "", "Brian L.", "brl"),
        User(2, "", "Lucas R.", "lc")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Initial ViewState should NOT be equals to notExpectedState ViewState`() = runBlockingTest {
        val notExpectedState = ViewState(isLoading = false, users = usersList)

        assert(viewModel.uiState.value != notExpectedState)
    }

    @Test
    fun `Update state with SUCCESS`() = runBlockingTest {
        val expectedState = ViewState(isLoading = false, users = usersList)

        coEvery { getUsers() } returns flow { emit(Result.Success(usersList)) }

        assert(viewModel.uiState.value == initialState)

        viewModel.run {
            reduceUsersList()

            uiState.test {
                val resultState = awaitItem()
                assert(resultState == expectedState)
                expectNoEvents()
            }

            events.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Update state with LOADING`() = runBlockingTest {
        val expectedState = ViewState(isLoading = true, users = emptyList())

        coEvery { getUsers() } returns flow { emit(Result.Loading) }

        assert(viewModel.uiState.value == initialState)

        viewModel.run {
            reduceUsersList()

            uiState.test {
                val resultState = awaitItem()
                assert(resultState == expectedState)
                expectNoEvents()
            }

            events.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Update state with IDLE`() = runBlockingTest {
        val expectedState = ViewState(isLoading = false, users = emptyList())

        coEvery { getUsers() } returns flow { emit(Result.Idle) }

        assert(viewModel.uiState.value == initialState)

        viewModel.run {
            reduceUsersList()

            uiState.test {
                val resultState = awaitItem()
                assert(resultState == expectedState)
                expectNoEvents()
            }

            events.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Update state with ERROR`() = runBlockingTest {
        val expectedState = ViewState(isLoading = false, users = emptyList())

        coEvery { getUsers() } returns flow { emit(Result.Error("test")) }

        assert(viewModel.uiState.value == initialState)

        viewModel.run {
            reduceUsersList()

            uiState.test {
                val resultState = awaitItem()
                assert(resultState == expectedState)
                expectNoEvents()
            }

            events.test {
                assert(awaitItem() is ViewEvent.ShowFetchErrorToast)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Update state with SUCCESS after LOADING state`() = runBlockingTest {
        val expectedInitialStateChange = ViewState(isLoading = true, users = emptyList())
        val expectedFinalStateChange = ViewState(isLoading = false, users = usersList)

        coEvery {
            getUsers()
        } returns flow {
            emit(Result.Loading)
            emit(Result.Success(usersList))
        }

        assert(viewModel.uiState.value == initialState)

        viewModel.run {
            uiState.test {
                reduceUsersList()

                awaitItem() // discard first view change
                val initialResultState = awaitItem()
                val finalResultState = awaitItem()

                assert(initialResultState == expectedInitialStateChange)
                assert(finalResultState == expectedFinalStateChange)

                expectNoEvents()
            }

            events.test {
                expectNoEvents()
            }
        }
    }
}