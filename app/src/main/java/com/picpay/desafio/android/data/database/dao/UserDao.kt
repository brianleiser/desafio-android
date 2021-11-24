package com.picpay.desafio.android.data.database.dao

import androidx.room.*
import com.picpay.desafio.android.data.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun readAll(): Flow<List<UserEntity>>
}