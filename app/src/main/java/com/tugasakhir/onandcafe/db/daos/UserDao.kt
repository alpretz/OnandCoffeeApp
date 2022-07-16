package com.tugasakhir.onandcafe.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tugasakhir.onandcafe.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("select count(*) from user where username=:value or email=:value")
    suspend fun getUserCountByUsernameEmail(value: String): Int

    @Query("select * from user where (username=:value or email=:value) and password=:password")
    suspend fun getUserByUsernameEmailAndPassword(value: String, password: String): User?

    @Query("select * from user where id=:id")
    suspend fun getUserById(id: String): User?
}