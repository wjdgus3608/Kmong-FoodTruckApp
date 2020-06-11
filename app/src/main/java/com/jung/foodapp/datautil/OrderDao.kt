package com.jung.foodapp.datautil

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jung.foodapp.data.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(vararg order: Order)

    @Delete
    fun deleteOrder(vararg order: Order)

    @Update
    fun updateOrder(vararg order: Order)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): LiveData<MutableList<Order>>

    @Query("DELETE FROM orders WHERE truckId = :id")
    fun deleteOrderByTruckId(id:Int)
}