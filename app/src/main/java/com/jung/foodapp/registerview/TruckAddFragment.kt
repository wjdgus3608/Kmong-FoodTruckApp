package com.jung.foodapp.registerview

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.core.view.marginRight
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.Food
import com.jung.foodapp.data.Truck
import com.jung.foodapp.databinding.DialogMenuAddBinding
import com.jung.foodapp.databinding.FragmentTruckAddBinding
import com.jung.foodapp.datautil.BitmapConverter
import com.jung.foodapp.datautil.DBRepo
import kotlinx.android.synthetic.main.chip_food.*
import kotlinx.android.synthetic.main.chip_food.view.*
import kotlinx.android.synthetic.main.chip_food.view.chip_delete_btn
import kotlinx.android.synthetic.main.dialog_menu_add.*
import kotlinx.android.synthetic.main.fragment_truck_add.*
import kotlinx.android.synthetic.main.item_preview_picture.view.*

class TruckAddFragment : Fragment(){

    var geocoder: Geocoder? = null
    var photoList= ArrayList<String>()
    var foodIdList= ArrayList<Int>()
    var foodPriceList= ArrayList<Long>()
    val model by lazy {
        ViewModelProvider(activity!!,ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=
            DataBindingUtil.inflate<FragmentTruckAddBinding>(inflater,
                R.layout.fragment_truck_add,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geocoder= Geocoder(context)
        foodIdList.clear()
        foodPriceList.clear()
        val popupView=DataBindingUtil.inflate<DialogMenuAddBinding>(layoutInflater,R.layout.dialog_menu_add,view as ViewGroup,false)
        popupView.setVariable(BR.vm,model)
        val builder = AlertDialog.Builder(context!!)

        add_search_btn.setOnClickListener {
            model.changeToSearchView()
        }
        var dialog:AlertDialog? = null
        register_menu_add_btn.setOnClickListener {
            if(dialog==null)
            dialog=builder.setView(popupView.root).show()
            else dialog!!.show()
        }
        popupView.dialogSubmit.setOnClickListener {
            val name = popupView.dialogFoodName.text
            val price = popupView.dialogFoodPrice.text
            if(name.isNotBlank() && price.isNotBlank()){
                val chipView = layoutInflater.inflate(R.layout.chip_food,register_menu_group,false)
                chipView.chip_delete_btn.setOnClickListener { register_menu_group.removeView(chipView) }
                chipView.chip_name.text = name
                register_menu_group.addView(chipView)
                foodPriceList.add(price.toString().toLong())
                dialog?.dismiss()
            }
            else
                Toast.makeText(context,"입력되지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show()
        }
        popupView.dialogCancel.setOnClickListener {
            dialog?.dismiss()
        }

        register_photo_btn.setOnClickListener {
            model.isPhotoActivityStart.postValue(true)
        }

        register_submit_btn.setOnClickListener {
            if(isAllInputed()) {
                val registerNum = add_number.text.toString()
                val name = add_name.text.toString()
                val phoneNumber = add_phone_number.text.toString()
                var location = ""
                if(model.searchedLocation.value != null)
                    location = "${model.searchedLocation.value!!.latitude},${model.searchedLocation.value!!.longitude}"
                val tmpTruck = model.searchTruckByRegisterNum(registerNum)
                if (tmpTruck != null) {
                    Toast.makeText(context,"이미 등록된 등록번호입니다.",Toast.LENGTH_SHORT).show()
                } else {
                    addFood()
                    addPhoto()
                    model.insertTruck(
                        Truck(DBRepo.truckIdx, registerNum, name,
                            phoneNumber, foodIdList, location, photoList, model.loginUserId!!
                        )
                    )
                    DBRepo.truckIdx++
                    model.changeToTrucklistView()
                }
            }
            else
                Toast.makeText(context,"입력하지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show()
        }
        model.searchedLocation.observe(this, Observer {
            if(it != null){
                val result = geocoder!!.getFromLocation(it.latitude,it.longitude,1)
                if(result !=null){
                    val name = result[0].getAddressLine(0)
                    add_location.text = name
                }
            }
        })
        model.resultPhoto.observe(this, Observer {
            if(it != null){
                val photoView = layoutInflater.inflate(R.layout.item_preview_picture,register_photo_container,false)
                photoView.pre_image_view.setImageBitmap(it as Bitmap)
                photoView.pre_image_delete_btn.setOnClickListener { register_photo_container.removeView(photoView) }
                register_photo_container.addView(photoView)
                model.resultPhoto.postValue(null)
            }
        })
    }


    private fun addFood(){
        val cnt = register_menu_group.childCount
        for(i in 0 until cnt){
            foodIdList.add(DBRepo.foodIdx)
            model.insertFood(Food(DBRepo.foodIdx,
                register_menu_group.get(i).chip_name.text.toString(),
                foodPriceList[i]))
            DBRepo.foodIdx++
        }
    }

    private fun addPhoto(){
        val cnt = register_photo_container.childCount

        for(i in 0 until cnt){
            val string = BitmapConverter.BitmapToString(register_photo_container.get(i).pre_image_view.drawable.toBitmap())
            photoList.add(string)
        }
    }

    private fun isAllInputed() :Boolean{
        val registerNum = add_number.text.toString()
        val name = add_name.text.toString()
        val phoneNumber = add_phone_number.text.toString()
        if (registerNum.isNullOrBlank() || name.isNullOrBlank() ||
                phoneNumber.isNullOrBlank() || register_menu_group.childCount<=0) return false
        return true
    }

}