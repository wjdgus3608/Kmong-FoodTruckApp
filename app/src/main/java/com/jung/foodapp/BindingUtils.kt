package com.jung.foodapp

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.data.Food
import com.jung.foodapp.data.Truck
import com.jung.foodapp.detailview.FoodAdapter
import com.jung.foodapp.listview.TruckAdapter

@BindingAdapter("bind_items","bind_model")
fun setBindItems(view : RecyclerView, mList : MutableLiveData<ArrayList<Truck>>, model: MainViewModel) {
    val adapter = view.adapter as? TruckAdapter
        ?: TruckAdapter(model)
            .apply { view.adapter = this }
    adapter.truckList = mList
    adapter.notifyDataSetChanged()
}

@BindingAdapter("bind_food_items","bind_model")
fun setFoodItems(view : RecyclerView, mList : MutableLiveData<ArrayList<Food>>, model: MainViewModel) {
    val adapter = view.adapter as? FoodAdapter
        ?: FoodAdapter(model)
            .apply { view.adapter = this }
    if(mList.value != null)
    adapter.foodList = mList
    adapter.notifyDataSetChanged()
}