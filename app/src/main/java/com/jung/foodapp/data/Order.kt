package com.jung.foodapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    var id:Int = -1,
    @ColumnInfo(name = "userId")
    var userId:Int = -1,
    @ColumnInfo(name = "truckId")
    var truckId:Int = -1,
    @ColumnInfo(name = "foods")
    var foods:MutableList<Int>?,
    @ColumnInfo(name = "price")
    var price:Long = -1,
    @ColumnInfo(name = "time")
    var time:Long = -1,
    @ColumnInfo(name = "food_count")
    var food_count:MutableList<Int>?
)