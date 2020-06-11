package com.jung.foodapp.detailview

import android.content.DialogInterface
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.Order
import com.jung.foodapp.databinding.FragmentDetailBinding
import com.jung.foodapp.datautil.BitmapConverter
import com.jung.foodapp.datautil.DBRepo
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_food.*
import kotlinx.android.synthetic.main.item_preview_picture.view.*

class TruckDetailFragment : Fragment(){

    var geocoder: Geocoder? = null

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
            DataBindingUtil.inflate<FragmentDetailBinding>(inflater,
                R.layout.fragment_detail,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geocoder = Geocoder(context)
        detail_back_btn.setOnClickListener {
            model.changeToTrucklistView()
        }
        detail_food_recyclerview.addItemDecoration(DividerItemDecoration(context,
            LinearLayoutManager.VERTICAL))
        detail_delete_btn.setOnClickListener {
            model.deleteTruck(model.selectedTruck.value!!)
            model.selectedTruck.postValue(null)
            model.selectedTruckFoods.postValue(null)
            model.changeToTrucklistView()
        }
        detail_order_btn.setOnClickListener {

            val totalPrice = (detail_food_recyclerview.adapter as FoodAdapter).getTotalPrice()
            val foods = (detail_food_recyclerview.adapter as FoodAdapter).retFoodList
            val foodCounts = (detail_food_recyclerview.adapter as FoodAdapter).retFoodCountList
            val dialog = AlertDialog.Builder(context!!)
                .setTitle("주문 확인")
                .setMessage("총 주문 금액은 ${totalPrice}원입니다. 주문하시겠습니까?")
                .setNegativeButton("확인",(object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if(foods.size>0) {
                            val order = Order(
                                DBRepo.orderIdx, model.loginUserId!!,
                                model.selectedTruck.value!!.id, foods
                                , totalPrice, System.currentTimeMillis(), foodCounts
                            )
                            model.insertOrder(order)
                            DBRepo.orderIdx++
                            Toast.makeText(context, "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else Toast.makeText(context, "주문내역이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }))
                .setPositiveButton("취소",null)
                .show()
        }

        model.selectedTruck.observe(this, Observer {
            if(it != null){
                detail_truck_name.text = it.name
                detail_truck_number.text = it.register_num
                detail_truck_phone_number.text = it.phone_num
                val parsed = it.location.split(",")
                if(parsed.size>1){
                    val result = geocoder!!.getFromLocation(parsed[0].toDouble(),parsed[1].toDouble(),1)
                    if(result != null){
                        detail_truck_address.text = result[0].getAddressLine(0)
                    }
                }
                if(it.foods != null)
                    model.updateSelectedTruckFoods(it.foods!!)
                if(it.photos != null){
                    for(str in it.photos!!.iterator()){
                        val photoView = layoutInflater.inflate(R.layout.item_preview_picture,detail_picture_wrapper,false)
                        val bitmap = BitmapConverter.StringToBitmap(str)
                        photoView.pre_image_view.setImageBitmap(bitmap)
                        photoView.pre_image_delete_btn.visibility = View.INVISIBLE
                        detail_picture_wrapper.addView(photoView)
                    }

                }
            }
        })
        model.selectedTruckFoods.observe(this, Observer {
            detail_food_recyclerview.adapter?.notifyDataSetChanged()
        })
    }
}