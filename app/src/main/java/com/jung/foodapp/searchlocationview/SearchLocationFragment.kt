package com.jung.foodapp.searchlocationview

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*

class SearchLocationFragment : Fragment(), OnMapReadyCallback {
    lateinit var mMap:GoogleMap
    var geocoder:Geocoder? = null
    var curAddress: Address? = null
    var keyword = MutableLiveData("")
    private var mLatitude: Double? = null
    private var mLongitude: Double? = null
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
            DataBindingUtil.inflate<FragmentSearchBinding>(inflater,
                R.layout.fragment_search,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geocoder = Geocoder(context)
        search_mapview.onCreate(savedInstanceState)
        search_mapview.getMapAsync(this)
        search_submit_btn.setOnClickListener {
            if(mLatitude != null && mLongitude != null){
                model.searchedLocation.postValue(LatLng(mLatitude!!,mLongitude!!))
                model.changeToTruckAddView()
            }
            else{
                Toast.makeText(context,"좌표가 선택되지 않았습니다.",Toast.LENGTH_SHORT).show()
            }
        }
        search_keyword.setOnClickListener { search_keyword.isIconified = false }
        search_keyword.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                keyword.postValue(query.toString())
                val result = geocoder!!.getFromLocationName(query.toString(),1)
                if(result !=null){
                    curAddress=geocoder!!.getFromLocationName(query.toString(),1)[0]
                }
                else{
                    Toast.makeText(context,"검색 결과를 찾을 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        keyword.observe(this, Observer {
            if(curAddress != null){
                mMap.clear()
                val location = LatLng(curAddress!!.latitude, curAddress!!.longitude)
                mLatitude=location.latitude
                mLongitude=location.longitude
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                val my= MarkerOptions().position(location).title("위치")
                mMap.addMarker(my)
            }
        })

    }


    override fun onStart() {
        super.onStart()
        search_mapview?.onStart()
    }
    override fun onResume() {
        super.onResume()
        search_mapview?.onResume()
    }
    override fun onPause() {
        super.onPause()
        search_mapview?.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        search_mapview?.onDestroy()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.setOnMapLongClickListener {
            mMap.clear()
            val location = LatLng(it.latitude, it.longitude)
            mLatitude=location.latitude
            mLongitude=location.longitude
            val my= MarkerOptions().position(location).title("위치")
            mMap.addMarker(my)
        }
    }
}