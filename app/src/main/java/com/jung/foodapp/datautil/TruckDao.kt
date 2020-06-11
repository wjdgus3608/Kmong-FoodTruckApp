package com.jung.foodapp.datautil

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jung.foodapp.data.Truck

@Dao
interface TruckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTruck(vararg truck: Truck)

    @Delete
    fun deleteTruck(vararg truck: Truck)

    @Update
    fun updateTruck(vararg truck: Truck)

    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): LiveData<MutableList<Truck>>
}