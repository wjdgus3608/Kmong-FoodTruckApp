package com.jung.foodapp.listview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.Truck
import com.jung.foodapp.databinding.FragmentTruckListBinding
import com.jung.foodapp.datautil.DBRepo
import kotlinx.android.synthetic.main.fragment_truck_list.*

class ListFragment :Fragment(),OnMapReadyCallback{

    var mMap:GoogleMap? = null
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
            DataBindingUtil.inflate<FragmentTruckListBinding>(inflater,R.layout.fragment_truck_list,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        list_searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.toString().isNotBlank())
                    filterTruck(query.toString())
                list_searchview.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrBlank()){
                    DBRepo.getAllTrucks().observe(this@ListFragment, Observer {
                        model.truckList.postValue(ArrayList(it))
                    })
                }
                return true
            }
        })
        list_searchview.setOnClickListener { list_searchview.isIconified = false }
        list_truck_add_btn.setOnClickListener {
            model.changeToTruckAddView()
        }
        list_show_buylist_btn.setOnClickListener {
            model.changeToOrderView()
        }
        list_recyclerview.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        model.searchedLocation.postValue(null)
        model.truckList.observe(this, Observer {
            list_recyclerview.adapter?.notifyDataSetChanged()
        })
        model.clickedTruckLocation.observe(this, Observer {
            moveMapToLocation(it)
        })

    }

    private fun filterTruck(keyword: String){
        val filteredList=ArrayList<Truck>()
        if(model.truckList.value!=null){
            for(i in 0 until model.truckList.value!!.size){
                val item=model.truckList.value!![i]
                if(item.register_num.contains(keyword) || item.name.contains(keyword)) {
                    filteredList.add(item)
                    continue
                }
                val targetFoods = model.searchFoodsByName(keyword)
                val foodIds = item.foods
                if(targetFoods != null && foodIds != null){
                    var flag = false
                    for(foodId in foodIds){
                        for(food in targetFoods){
                            if(foodId == food.id){
                                filteredList.add(item)
                                flag = true
                                break
                            }
                        }
                        if(flag) break;
                    }
                }
            }
        }
        model.truckList.postValue(filteredList)

    }

    private fun moveMapToLocation(location:LatLng){
        if(mMap!=null) {
            mMap!!.clear()
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(location))
            mMap!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
            val my = MarkerOptions().position(location).title("위치")
            mMap!!.addMarker(my)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap=p0!!
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.566,126.978)))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

}