package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.databinding.ActivityBuyerHomeBinding
import com.example.cotacaofacil.databinding.ActivityProviderHomeBinding
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.ui.fragment.buyer.HomeBuyerFragment
import com.example.cotacaofacil.presentation.util.BottomNavigationListener
import org.koin.android.ext.android.inject

class HomeProviderActivity : AppCompatActivity(), BottomNavigationListener {
    private val userHelper by inject<UserHelper>()
    val user by lazy { userHelper.user }
    private lateinit var binding: ActivityProviderHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        supportFragmentManager.findFragmentById(R.id.container)?.let {
            binding.bottomNavigationProvider.setupWithNavController(
                it.findNavController()
            )
        }
    }

    private fun changeToSecondFragment(idFragment: Int) {
        try {
            binding.bottomNavigationProvider.selectedItemId = idFragment
        } catch (e : Exception){
            binding.bottomNavigationProvider.selectedItemId = R.id.fragment_provider_buyer
        }
    }

    override fun onResume() {
        if(user == null){
            //criar uma viewmodel e se for nulo buscar o user da mesma forma que o login buscou para passar para cá
            finish()
        }
        super.onResume()
    }


    companion object {
        const val USER = "user"
    }

    override fun onChangeFragmentBottomNavigation(idFragment: Int) {
        changeToSecondFragment(idFragment)
    }
}