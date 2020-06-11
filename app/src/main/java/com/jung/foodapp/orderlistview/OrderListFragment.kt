package com.jung.foodapp.orderlistview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.databinding.FragmentOrderListBinding
import kotlinx.android.synthetic.main.fragment_order_list.*
import kotlinx.android.synthetic.main.fragment_order_list.view.*
import kotlinx.android.synthetic.main.fragment_order_list.view.order_list_back_btn
import kotlinx.android.synthetic.main.item_order.view.*
import kotlinx.android.synthetic.main.item_order_food.view.*

class OrderListFragment :Fragment(){

    val model by lazy {
        ViewModelProvider(activity!!, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=
            DataBindingUtil.inflate<FragmentOrderListBinding>(inflater,
                R.layout.fragment_order_list,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        order_list_back_btn.setOnClickListener {
            model.changeToTrucklistView()
        }

        model.orderList.observe(this, Observer {
            view.order_container.removeAllViews()
            for(i in 0 until it.size){
                if(it[i].userId == model.loginUserId){
                    val item = it[i]
                    val tmpView = layoutInflater.inflate(R.layout.item_order,view.order_container,false)
                    tmpView.order_cancel_order_btn.setOnClickListener {
                        model.deleteOrder(item)
//                        view.order_container.removeView(tmpView)
                    }
                    tmpView.order_item_truck_name.text = model.searchTruckById(item.truckId)?.name
                    tmpView.order_item_total_price.text = "총액 ${item.price} 원"
                    for(j in 0 until item.foods!!.size){
                        val foodId = item.foods!![j]
                        val food = model.searchFoodById(foodId)
                        val foodView = layoutInflater.inflate(R.layout.item_order_food,tmpView.order_food_container,false)
                        foodView.item_food_name.text = food!!.name
                        foodView.item_food_price.text = food.price.toString()
                        foodView.item_food_count.text = item.food_count!![j].toString()
                        tmpView.order_food_container.addView(foodView)
                    }
                    view.order_container.addView(tmpView)
                }
            }
        })
    }
}