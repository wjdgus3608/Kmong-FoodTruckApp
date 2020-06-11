package com.jung.foodapp

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.jung.foodapp.data.Truck
import com.jung.foodapp.databinding.ActivityMainBinding
import com.jung.foodapp.datautil.AppDatabase
import com.jung.foodapp.datautil.DBRepo
import com.jung.foodapp.detailview.TruckDetailFragment
import com.jung.foodapp.listview.ListFragment
import com.jung.foodapp.registerview.TruckAddFragment
import com.jung.foodapp.loginview.LoginFragment
import com.jung.foodapp.loginview.SigninFragment
import com.jung.foodapp.orderlistview.OrderListFragment
import com.jung.foodapp.searchlocationview.SearchLocationFragment

class MainActivity : AppCompatActivity() {

    lateinit var model: MainViewModel
    private var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding= DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        model= ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)
        binding.setVariable(BR.vm, model)
        binding.setLifecycleOwner { this.lifecycle }

        initDB()
        loadDB()

        model.isPhotoActivityStart.observe(this, Observer {
            if(it){
                startPhotoActivity()
            }
        })
        model.userList.observe(this, Observer {
            Log.d("log","userList size : ${it.size}")
        })
        model.truckList.observe(this, Observer {
            Log.d("log","truckList size : ${it.size}")
            model.initTruckIdx()
        })
        model.foodList.observe(this, Observer {
            Log.d("log","foodList size : ${it.size}")
            model.initFoodIdx()
        })
        model.orderList.observe(this, Observer {
            Log.d("log","orderList size : ${it.size}")
            model.initOrderIdx()
        })
        model.viewMode.observe(this, Observer {
            when(it){
                0->supportFragmentManager
                    .beginTransaction().replace(R.id.main_container,
                        LoginFragment()
                    )
                    .commit()
                1->supportFragmentManager
                    .beginTransaction().replace(R.id.main_container,
                        SigninFragment()
                    )
                    .commit()
                2->supportFragmentManager
                    .beginTransaction().replace(R.id.main_container,
                        ListFragment()
                    )
                    .commit()
                3->supportFragmentManager
                    .beginTransaction().replace(R.id.main_container,
                        TruckAddFragment()
                    )
                    .commit()
                4->supportFragmentManager
                    .beginTransaction().replace(R.id.main_container,
                        SearchLocationFragment()
                    )
                    .commit()
                5->supportFragmentManager
                    .beginTransaction().add(R.id.main_container,
                        TruckDetailFragment()
                    )
                    .commit()
                6->supportFragmentManager
                    .beginTransaction().add(R.id.main_container,
                        OrderListFragment()
                    )
                    .commit()
            }

        })
    }

    private fun startPhotoActivity(){
        if(ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 100)
            }
            else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),0)
        }
    }

    private fun initDB(){
        DBRepo.db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java,"AppDatabase.db").build()
    }



    override fun onBackPressed() {
        if(model.viewMode.value == 0){
            if(isBackPressed)
                super.onBackPressed()
            isBackPressed=true
            Toast.makeText(this,"한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ isBackPressed=false },2000)
        }
        else {
            supportFragmentManager.popBackStack()
            when(model.viewMode.value!!){
                1->model.changeToLoginView()
                2->model.changeToLoginView()
                3->model.changeToTrucklistView()
                4->model.changeToTruckAddView()
                5->model.changeToTrucklistView()
                6->model.changeToTrucklistView()
            }

        }
    }

    private fun loadDB(){
        DBRepo.getAllUsers().observe(this, Observer {
            model.userList.postValue(ArrayList(it))
        })
        DBRepo.getAllFoods().observe(this, Observer {
            model.foodList.postValue(ArrayList(it))
        })
        DBRepo.getAllTrucks().observe(this, Observer {
            val tmpList = ArrayList<Truck>()
            for(truck in it.iterator()){
                if(model.loginUserId !=null && truck.userId == model.loginUserId)
                    tmpList.add(truck)
            }
            model.truckList.postValue(tmpList)
        })
        DBRepo.getAllOrders().observe(this, Observer {
            model.orderList.postValue(ArrayList(it))
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return
        model.isPhotoActivityStart.postValue(false)
        if(requestCode == 100){
            Log.d("log",data?.extras?.get("data").toString())
            model.resultPhoto.postValue(data?.extras?.get("data"))
        }
    }
}
