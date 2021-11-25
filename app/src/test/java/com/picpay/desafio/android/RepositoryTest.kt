package com.picpay.desafio.android

import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.Result
import com.picpay.desafio.android.domain.repository.UserRepository
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test
import app.cash.turbine.test
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.picpay.desafio.android.data.database.model.UserEntity
import com.picpay.desafio.android.data.mapper.mapToDomainModel
import io.mockk.every
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After

@ExperimentalCoroutinesApi
class RepositoryTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val store = mockk<Store<String, List<UserEntity>>>()
    private val repository: UserRepository = UserRepositoryImpl(store, dispatcher)

    @After
    fun tearDown() {
        dispatcher.cleanupTestCoroutines()
    }

    private val usersEntityList = listOf(
        UserEntity(1, "", "Brian L.", "brl"),
        UserEntity(2, "", "Lucas R.", "lc")
    )

    @Test
    fun `Get users with SUCCESS result`() = runBlockingTest {
        every {
            store.stream(StoreRequest.cached(key = "get_users", refresh = true))
        } returns flow {
            emit(
                StoreResponse.Data(
                    origin = ResponseOrigin.Fetcher,
                    value = usersEntityList
                )
            )
        }

        repository.getUsers().test {
            val result = awaitItem() as Result.Success
            assert(result.data.size == usersEntityList.size)
            assert(result.data.containsAll(usersEntityList.mapToDomainModel()))
            awaitComplete()
        }
    }

    @Test
    fun `Get users with SUCCESS after LOADING state`() = runBlockingTest {
        every {
            store.stream(StoreRequest.cached(key = "get_users", refresh = true))
        } returns flow {
            emit(StoreResponse.Loading(origin = ResponseOrigin.Fetcher))
            emit(
                StoreResponse.Data(
                    origin = ResponseOrigin.Fetcher,
                    value = usersEntityList
                )
            )
        }

        repository.getUsers().test {
            val initialState = awaitItem()
            val finalResult = awaitItem() as Result.Success
            assert(initialState is Result.Loading)
            assert(finalResult.data.size == usersEntityList.size)
            assert(finalResult.data.containsAll(usersEntityList.mapToDomainModel()))
            awaitComplete()
        }
    }

    @Test
    fun `Get users with LOADING state`() = runBlockingTest {
        every {
            store.stream(StoreRequest.cached(key = "get_users", refresh = true))
        } returns flow {
            emit(StoreResponse.Loading(origin = ResponseOrigin.Fetcher))
        }

        repository.getUsers().test {
            val state = awaitItem()
            assert(state is Result.Loading)
            awaitComplete()
        }
    }

    @Test
    fun `Get users with ERROR result`() = runBlockingTest {
        every {
            store.stream(StoreRequest.cached(key = "get_users", refresh = true))
        } returns flow {
            emit(
                StoreResponse.Error.Exception(
                    origin = ResponseOrigin.Fetcher,
                    error = NullPointerException()
                )
            )
        }

        repository.getUsers().test {
            val state = awaitItem()
            assert(state is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `Get users with IDLE state`() = runBlockingTest {
        every {
            store.stream(StoreRequest.cached(key = "get_users", refresh = true))
        } returns flow {
            emit(StoreResponse.NoNewData(origin = ResponseOrigin.Fetcher))
        }

        repository.getUsers().test {
            val state = awaitItem()
            assert(state is Result.Idle)
            awaitComplete()
        }
    }
}