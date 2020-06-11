package com.jung.foodapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    @ColumnInfo(name = "login_id")
    var login_id:String = "",
    @ColumnInfo(name = "login_pw")
    var login_pw:String = "",
    @ColumnInfo(name = "name")
    var name:String = "",
    @ColumnInfo(name = "age")
    var age:Int = -1
//    @ColumnInfo(name = "orders")
//    var orders:MutableList<Int>?,
//    @ColumnInfo(name = "trucks")
//    var trucks:MutableList<Int>?
)