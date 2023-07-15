package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cotacaofacil.databinding.ActivityCreatePriceBinding

class CreatePriceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePriceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePriceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object{
        const val SUCCESS_CREATE_PRICE = 2606
    }

}