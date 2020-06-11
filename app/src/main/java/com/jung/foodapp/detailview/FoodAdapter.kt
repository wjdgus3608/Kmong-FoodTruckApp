package com.jung.foodapp.detailview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jung.foodapp.BR
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.Food
import com.jung.foodapp.databinding.ItemFoodBinding
import kotlinx.android.synthetic.main.item_food.view.*

class FoodAdapter(parentModel: MainViewModel) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(){

    val model = parentModel
    var foodList = MutableLiveData<ArrayList<Food>>()
    private var countList = HashMap<Int,Int>()
    var retFoodList = ArrayList<Int>()
    var retFoodCountList = ArrayList<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding:ItemFoodBinding= DataBindingUtil.inflate(inflater,
            R.layout.item_food,parent,false)
        binding.setVariable(BR.vm,model)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if(foodList.value == null) return 0
        return foodList.value!!.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        foodList.value!![position].let { item -> with(holder) {
            foodNameView.text = item.name
            foodPriceView.text = "₩ ${item.price}"

        }}
        holder.itemView.item_food_picker.maxValue = 10
        holder.itemView.item_food_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            countList[position]=newVal
        }
    }

    fun getTotalPrice():Long{
        var total:Long = 0
        retFoodList.clear()
        retFoodCountList.clear()
        for(pos in countList.keys){
            if(countList[pos]==0) continue
            retFoodList.add(foodList.value!![pos].id)
            retFoodCountList.add(countList[pos]!!)
            total += foodList.value!![pos].price * countList[pos]!!
        }
        return total
    }

    inner class FoodViewHolder(binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        val foodNameView = itemView.item_food_name
        val foodPriceView = itemView.item_food_price
    }
}