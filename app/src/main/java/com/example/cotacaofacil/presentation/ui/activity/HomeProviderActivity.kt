package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cotacaofacil.databinding.ActivityProviderHomeBinding
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.ui.fragment.buyer.HomeBuyerFragment

class HomeProviderActivity : AppCompatActivity() {
    val user by lazy { intent?.extras?.getParcelable<UserModel>(USER) }
    private lateinit var binding: ActivityProviderHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderHomeBinding.inflate(layoutInflater)
        binding.container.getFragment<HomeBuyerFragment>()
        setContentView(binding.root)
        setupListeners()

    }

    private fun setupListeners() {


    }

    companion object {
        const val USER = "user"
    }
}