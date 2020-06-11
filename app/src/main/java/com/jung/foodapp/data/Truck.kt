package com.jung.foodapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class Truck(
    @PrimaryKey(autoGenerate = true)
    var id:Int = -1,
    @ColumnInfo(name = "register_num")
    var register_num:String = "",
    @ColumnInfo(name = "name")
    var name:String = "",
    @ColumnInfo(name = "phone_num")
    var phone_num:String = "",
    @ColumnInfo(name = "foods")
    var foods:MutableList<Int>?,
    @ColumnInfo(name = "location")
    var location:String = "",
    @ColumnInfo(name = "photos")
    var photos:MutableList<String>?,
    @ColumnInfo(name = "userId")
    var userId:Int
)