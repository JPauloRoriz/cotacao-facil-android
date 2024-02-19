package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.databinding.ActivityCreatePriceBinding
import com.example.cotacaofacil.domain.model.UserModel
import org.koin.android.ext.android.inject

class CreatePriceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePriceBinding
    private val userHelper by inject<UserHelper>()
    val user by lazy { userHelper.user }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePriceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        if(user == null){
            //criar uma viewmodel e se for nulo buscar o user da mesma forma que o login buscou para passar para cá
            finish()
        }
        super.onResume()
    }

    companion object{
        const val SUCCESS_CREATE_PRICE = 2606
    }

}