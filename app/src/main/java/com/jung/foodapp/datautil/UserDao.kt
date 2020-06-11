package com.jung.foodapp.datautil

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jung.foodapp.data.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: User)

    @Delete
    fun deleteUser(vararg user: User)

    @Update
    fun updateUser(vararg user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers():LiveData<MutableList<User>>

    @Query("SELECT * FROM users WHERE login_id=:id")
    fun getUserByLoginId(id:String):User
}