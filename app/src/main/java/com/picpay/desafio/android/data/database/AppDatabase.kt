package com.picpay.desafio.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.picpay.desafio.android.data.database.dao.UserDao
import com.picpay.desafio.android.data.database.model.UserEntity

@Database(entities = [UserEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
}