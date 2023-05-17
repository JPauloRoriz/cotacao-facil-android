package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.databinding.ActivityBuyerHomeBinding
import com.example.cotacaofacil.presentation.util.BottomNavigationListener
import org.koin.android.ext.android.inject


class HomeBuyerActivity : AppCompatActivity(), BottomNavigationListener {
    private val userHelper by inject<UserHelper>()
    val user by lazy { userHelper.user }
    private lateinit var binding: ActivityBuyerHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        supportFragmentManager.findFragmentById(R.id.container)?.let {
            binding.bottomNavigation.setupWithNavController(
                it.findNavController()
            )
        }
    }

    private fun changeToSecondFragment(idFragment: Int) {
        try {
            binding.bottomNavigation.selectedItemId = idFragment
        } catch (e : Exception){
            binding.bottomNavigation.selectedItemId = R.id.fragment_home_buyer
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