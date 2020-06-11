package com.jung.foodapp.listview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.jung.foodapp.BR
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.Truck
import com.jung.foodapp.databinding.ItemTruckBinding
import kotlinx.android.synthetic.main.item_truck.view.*

class TruckAdapter(parentModel: MainViewModel) : RecyclerView.Adapter<TruckAdapter.TruckViewHolder>(){

    val model = parentModel
    var truckList = MutableLiveData<ArrayList<Truck>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding:ItemTruckBinding= DataBindingUtil.inflate(inflater,
            R.layout.item_truck,parent,false)
        binding.setVariable(BR.vm,model)
        return TruckViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if(truckList.value == null) return 0
        return truckList.value!!.size
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        truckList.value!![position].let { item -> with(holder) {

            truckNameView.text = item.name
            truckNumView.text = item.register_num

            if(item.foods!!.size>0){
                var str = ""
                val food = model.searchFoodById(item.foods!![0])
                str += food?.name
                if(item.foods!!.size>1)
                    str += " 외 ${item.foods!!.size-1}종"
                foodNameView.text = str
            }
        }}
        holder.itemView.setOnClickListener {
            val str = truckList.value!![position].location
            if(str.isNotBlank()){
                val parsed = str.split(",")
                val location = LatLng(parsed[0].toDouble(),parsed[1].toDouble())
                model.clickedTruckLocation.postValue(location)
            }
        }
        holder.itemView.item_detail_btn.setOnClickListener {
            model.selectedTruck.postValue(truckList.value!![position])
            model.changeToDetailView()
        }
    }

    inner class TruckViewHolder(binding: ItemTruckBinding) : RecyclerView.ViewHolder(binding.root) {
        val truckNumView: TextView = itemView.item_truck_number
        val truckNameView: TextView = itemView.item_truck_name
        val foodNameView: TextView = itemView.item_food_name
    }
}