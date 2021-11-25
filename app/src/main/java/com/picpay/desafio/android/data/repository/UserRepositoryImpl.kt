package com.picpay.desafio.android.data.repository

import com.dropbox.android.external.store4.*
import com.picpay.desafio.android.data.database.model.UserEntity
import com.picpay.desafio.android.data.mapper.mapToDomainModel
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.Result
import com.picpay.desafio.android.util.extensions.orElse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val userStore: Store<String, List<UserEntity>>,
    private val dispatcher: CoroutineDispatcher
): UserRepository {

    private companion object {
        const val UNKNOWN_EXCEPTION = "Unknown"
    }

    override suspend fun getUsers(): Flow<Result<List<User>>> {
        return flow {
            userStore.stream(StoreRequest.cached(key = "get_users", refresh = true))
                .flowOn(dispatcher)
                .collect { response ->
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
        }.flowOn(dispatcher)
    }
}