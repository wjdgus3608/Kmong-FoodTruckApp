package com.jung.foodapp.datautil

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.jung.foodapp.data.Food
import com.jung.foodapp.data.Order
import com.jung.foodapp.data.Truck
import com.jung.foodapp.data.User

@Database(entities = [(User::class),(Food::class),(Truck::class),(Order::class)], version = 2)
@TypeConverters(ListConverter::class,PhotoConverter::class)
abstract class AppDatabase :RoomDatabase(){
    abstract fun UserDao(): UserDao
    abstract fun FoodDao(): FoodDao
    abstract fun TruckDao(): TruckDao
    abstract fun OrderDao(): OrderDao

}