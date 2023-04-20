package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.databinding.FragmentHomeBuyerBinding
import com.example.cotacaofacil.presentation.ui.activity.HomeBuyerActivity
import com.example.cotacaofacil.presentation.ui.activity.HomeProviderActivity
import com.example.cotacaofacil.presentation.ui.dialog.AddProductBottomSheetDialogFragment
import com.example.cotacaofacil.presentation.viewmodel.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeBuyerFragment : Fragment() {
    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: FragmentHomeBuyerBinding
    private val user by lazy { (activity as? HomeBuyerActivity)?.user }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
    }

    private fun setupListeners() {
        binding.cardViewStock.setOnClickListener {
            val addProductBottomSheetDialogFragment = AddProductBottomSheetDialogFragment.newInstance(user?.cnpj ?: "erro")
            activity?.supportFragmentManager?.let { it1 -> addProductBottomSheetDialogFragment.show(it1, "") }
        }
    }
}