package com.picpay.desafio.android.data.repository

import android.util.Log
import com.dropbox.android.external.store4.*
import com.picpay.desafio.android.data.database.dao.UserDao
import com.picpay.desafio.android.data.database.model.UserEntity
import com.picpay.desafio.android.data.mapper.mapToDomainModel
import com.picpay.desafio.android.data.mapper.mapToEntityModel
import com.picpay.desafio.android.data.network.model.UserResponse
import com.picpay.desafio.android.data.network.service.UserService
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.Result
import com.picpay.desafio.android.util.extensions.orElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@FlowPreview
@ExperimentalCoroutinesApi
class UserRepositoryImpl(
    private val service: UserService,
    private val userDao: UserDao
): UserRepository {

    private companion object {
        const val LOG_TAG = "UserRepository"
        const val UNKNOWN_EXCEPTION = "Unknown"
    }

    private val store: Store<String, List<UserEntity>> = StoreBuilder.from(
        fetcher = Fetcher.of { _: String -> service.getUsers() },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { userDao.readAll() },
            writer = { _: String, serviceResponse: List<UserResponse> ->
                val users = serviceResponse.mapToEntityModel()
                userDao.insertAll(users)
            }
        )
    ).build()

    override suspend fun getUsers(): Flow<Result<List<User>>> {
        return flow {
            store.stream(StoreRequest.cached(key = "get_users", refresh = true))
                .flowOn(Dispatchers.IO)
                .collect { response ->
                    Log.d(LOG_TAG, "Operation origin => ${response.origin.name}")
                    Log.d(LOG_TAG, "Operation state => $response")
                    when (response) {
                        is StoreResponse.Loading -> emit(Result.Loading)
                        is StoreResponse.NoNewData -> emit(Result.Idle)
                        is StoreResponse.Data -> emit(Result.Success(response.value.mapToDomainModel()))
                        is StoreResponse.Error -> {
                            val errorMessage = response
                                .errorMessageOrNull()
                                .orElse(UNKNOWN_EXCEPTION)

                            emit(Result.Error(errorMessage))
                        }
                    }
                }
        }.flowOn(Dispatchers.IO)
    }
}