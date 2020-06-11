package com.jung.foodapp.datautil

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jung.foodapp.data.Food

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(vararg food: Food)

    @Delete
    fun deleteFood(vararg food: Food)

    @Update
    fun updateFood(vararg food: Food)

    @Query("SELECT * FROM foods")
    fun getAllFoods(): LiveData<MutableList<Food>>

    @Query("DELETE FROM foods WHERE id = :id")
    fun deleteFoodById(id:Int)
}