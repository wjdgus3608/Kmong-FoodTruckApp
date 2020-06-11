package com.jung.foodapp.datautil

import android.util.Log
import androidx.lifecycle.LiveData
import com.jung.foodapp.data.Food
import com.jung.foodapp.data.Order
import com.jung.foodapp.data.Truck
import com.jung.foodapp.data.User
import kotlinx.coroutines.*

object DBRepo {
    var db:AppDatabase? = null
    var truckIdx = 1
    var foodIdx = 1
    var orderIdx = 1

    fun getAllUsers() : LiveData<MutableList<User>> = db!!.UserDao().getAllUsers()
    fun getAllFoods() : LiveData<MutableList<Food>> = db!!.FoodDao().getAllFoods()
    fun getAllTrucks() : LiveData<MutableList<Truck>>  =db!!.TruckDao().getAllTrucks()
    fun getAllOrders() : LiveData<MutableList<Order>> = db!!.OrderDao().getAllOrders()

    fun insertUser(user :User) {
        Thread(Runnable {
            db!!.UserDao().insertUser(user)
        }).start()
    }

    fun insertFood(food :Food){
        Thread(Runnable {
            db!!.FoodDao().insertFood(food)
        }).start()
    }

    fun insertTruck(truck :Truck){
        Thread(Runnable {
            db!!.TruckDao().insertTruck(truck)
        }).start()
    }

    fun insertOrder(order :Order){
        Thread(Runnable {
            db!!.OrderDao().insertOrder(order)
        }).start()
    }

    fun deleteTruck(truck: Truck){
        Thread(Runnable {
            db!!.TruckDao().deleteTruck(truck)
        }).start()
        val tmpList = truck.foods
        if(tmpList != null){
            for(id in tmpList){
                deleteFoodById(id)
            }
        }
        deleteOrderByTruckId(truck.id)
    }

    fun deleteOrder(order: Order){
        Thread(Runnable {
            db!!.OrderDao().deleteOrder(order)
        }).start()
    }

    fun deleteFoodById(id: Int){
        Thread(Runnable {
            db!!.FoodDao().deleteFoodById(id)
        }).start()
    }

    fun deleteOrderByTruckId(id: Int){
        Thread(Runnable {
            db!!.OrderDao().deleteOrderByTruckId(id)
        }).start()
    }

}