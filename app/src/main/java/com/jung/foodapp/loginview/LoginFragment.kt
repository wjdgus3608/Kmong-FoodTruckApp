package com.jung.foodapp.loginview

import android.drm.DrmInfoStatus
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.Truck
import com.jung.foodapp.data.User
import com.jung.foodapp.databinding.FragmentLoginBinding
import com.jung.foodapp.datautil.DBRepo
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment(){

    private var id:String? = null
    private var pw:String? = null
    private var user: User? = null

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
            DataBindingUtil.inflate<FragmentLoginBinding>(inflater,R.layout.fragment_login,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signin_btn.setOnClickListener {
            model.changeToLSigninView()
        }
        login_btn.setOnClickListener {
            id = id_input.text.toString()
            pw = pw_input.text.toString()

            if(id.isNullOrBlank() || pw.isNullOrBlank()){
                Toast.makeText(context,"아이디 또는 비밀번호를 입력하지 않았습니다.",Toast.LENGTH_SHORT).show()
            }
            else{
                if(checkLoginInfo()){
                    model.loginUserId = user!!.id
                    loadDB()
                   model.changeToTrucklistView()
                    Toast.makeText(context,"로그인 되었습니다.",Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(context,"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun checkLoginInfo():Boolean{
        user = model.searchUserByLoginId(id!!)
        if(user == null) return false
        if(user!!.login_pw != pw) return false
        return true
    }

    fun loadDB(){
        DBRepo.getAllTrucks().observe(this, Observer {
            val tmpList = ArrayList<Truck>()
            for(truck in it.iterator()){
                if(model.loginUserId !=null && truck.userId == model.loginUserId)
                    tmpList.add(truck)
            }
            Log.d("log","load db")
            model.truckList.postValue(tmpList)
        })
    }
}