package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.databinding.ActivityBuyerHomeBinding
import org.koin.android.ext.android.inject


class HomeBuyerActivity : AppCompatActivity() {
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

    companion object {
        const val USER = "user"
    }
}