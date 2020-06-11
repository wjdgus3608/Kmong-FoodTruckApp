package com.jung.foodapp.loginview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jung.foodapp.MainViewModel
import com.jung.foodapp.R
import com.jung.foodapp.data.User
import com.jung.foodapp.databinding.FragmentSigninBinding
import com.jung.foodapp.datautil.DBRepo
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SigninFragment : Fragment(){

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
            DataBindingUtil.inflate<FragmentSigninBinding>(inflater,R.layout.fragment_signin,container,false)
        binding.setVariable(BR.vm,model)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submit_btn.setOnClickListener {

            val name: String? = signin_name.text.toString()
            val age: Int? = signin_age.text.toString().toInt()
            val id: String? = signin_id.text.toString()
            val pw: String? = signin_pw.text.toString()

            if(name.isNullOrBlank() || age == null || id.isNullOrBlank() || pw.isNullOrBlank()){
                Toast.makeText(context,"입력하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show()
            }
            else{

                if(model.searchUserByLoginId(id)!=null)
                    Toast.makeText(context,"존재하는 ID입니다.", Toast.LENGTH_SHORT).show()
                else{
                    model.insertUser(User(0,id,pw,name,age))
                    Toast.makeText(context,"회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    model.changeToLoginView()
                }
            }
        }
    }

}