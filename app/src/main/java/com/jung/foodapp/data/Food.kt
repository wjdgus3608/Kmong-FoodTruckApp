package com.jung.foodapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    var id:Int = -1,
    @ColumnInfo(name = "name")
    var name:String = "",
    @ColumnInfo(name = "price")
    var price:Long = -1
)