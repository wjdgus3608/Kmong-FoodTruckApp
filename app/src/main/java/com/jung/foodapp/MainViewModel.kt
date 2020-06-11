package com.jung.foodapp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.jung.foodapp.data.Food
import com.jung.foodapp.data.Order
import com.jung.foodapp.data.Truck
import com.jung.foodapp.data.User
import com.jung.foodapp.datautil.DBRepo

class MainViewModel : ViewModel(){
    val userList = MutableLiveData(ArrayList<User>())
    val foodList = MutableLiveData(ArrayList<Food>())
    val truckList = MutableLiveData(ArrayList<Truck>())
    val orderList = MutableLiveData(ArrayList<Order>())
    var loginUserId:Int? = null
    var viewMode = MutableLiveData(0)
    var searchedLocation = MutableLiveData<LatLng>()
    var clickedTruckLocation = MutableLiveData<LatLng>()
    var selectedTruck = MutableLiveData<Truck>(null)
    var selectedTruckFoods = MutableLiveData(ArrayList<Food>())
    var isPhotoActivityStart = MutableLiveData(false)
    var resultPhoto = MutableLiveData<Any>(null)

    fun searchUserByLoginId(id: String) :User?{
        val tmpList = userList.value
        if(tmpList.isNullOrEmpty()) return null
        for(user in tmpList.iterator()){
            if(user.login_id == id) return user
        }
        return null
    }

    fun searchTruckByRegisterNum(num: String) :Truck?{
        val tmpList = truckList.value
        if(tmpList.isNullOrEmpty()) return null
        for(truck in tmpList.iterator()){
            if(truck.register_num == num) return truck
        }
        return null
    }

    fun searchTruckById(num: Int) :Truck?{
        val tmpList = truckList.value
        if(tmpList.isNullOrEmpty()) return null
        for(truck in tmpList.iterator()){
            if(truck.id == num) return truck
        }
        return null
    }

    fun searchFoodById(id: Int) :Food? {
        if (foodList.value == null) return null
        for(food in foodList.value!!.iterator()){
            if(food.id == id){
                return food
            }
        }
        return null
    }

    fun searchFoodsByName(name: String) :ArrayList<Food>? {
        if (foodList.value == null) return null
        val tmpList = ArrayList<Food>()
        for(food in foodList.value!!.iterator()){
            if(food.name.contains(name)){
                tmpList.add(food)
            }
        }
        if(tmpList.size == 0) return null
        return tmpList
    }

    fun updateSelectedTruckFoods(foodIds: MutableList<Int>){
        val tmpList = ArrayList<Food>()
        if(foodList.value == null || foodIds.size<=0) return
        for(foodId in foodIds){
            for(food in foodList.value!!){
                if(food.id == foodId){
                    tmpList.add(food)
                    break
                }
            }
        }
        selectedTruckFoods.postValue(tmpList)
    }

    fun changeToLoginView() = viewMode.postValue(0)
    fun changeToLSigninView() = viewMode.postValue(1)
    fun changeToTrucklistView() {
        selectedTruck.postValue(null)
        viewMode.postValue(2)
    }
    fun changeToTruckAddView() = viewMode.postValue(3)
    fun changeToSearchView() = viewMode.postValue(4)
    fun changeToDetailView() = viewMode.postValue(5)
    fun changeToOrderView() = viewMode.postValue(6)

    fun insertUser(user: User){
        DBRepo.insertUser(user)
    }
    fun insertTruck(truck: Truck){
        DBRepo.insertTruck(truck)
    }
    fun insertFood(food: Food){
        DBRepo.insertFood(food)
    }
    fun insertOrder(order: Order){
        DBRepo.insertOrder((order))
    }
    fun deleteTruck(truck: Truck){
        DBRepo.deleteTruck(truck)
    }
    fun deleteOrder(order: Order){
        DBRepo.deleteOrder(order)
    }
    fun initTruckIdx(){
        if (truckList.value!!.size>0){
            truckList.value?.sortBy { it.id }
            DBRepo.truckIdx = truckList.value!!.last().id + 1
        }
    }
    fun initFoodIdx(){
        if(foodList.value!!.size>0){
            foodList.value?.sortBy { it.id }
            DBRepo.foodIdx = foodList.value!!.last().id + 1
        }
    }
    fun initOrderIdx(){
        if(orderList.value!!.size>0){
            orderList.value?.sortBy { it.id }
            DBRepo.orderIdx = orderList.value!!.last().id + 1
        }
    }

}