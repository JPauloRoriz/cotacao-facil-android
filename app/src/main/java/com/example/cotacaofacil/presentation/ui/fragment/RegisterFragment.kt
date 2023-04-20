package com.example.cotacaofacil.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentRegisterBinding
import com.example.cotacaofacil.presentation.util.Maks
import com.example.cotacaofacil.presentation.viewmodel.register.RegisterViewModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.RegisterEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private val viewModel by viewModel<RegisterViewModel>()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                viewModel.tapOnRegister(
                    binding.edtInputCnpj.text.toString(),
                    binding.edtInputLogin.text.toString(),
                    binding.edtInputPassword.text.toString(),
                    binding.edtInputConfirmPassword.text.toString()
                )
            }
        }

        binding.imageViewBuyer.setOnClickListener {
            viewModel.tapOnUserBuyerSelected()
        }
        binding.imageViewProvider.setOnClickListener {
            viewModel.tapOnUserProviderSelected()
        }



        viewModel.enterCnpj()

    }

    private fun setupObservers() {

        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            binding.pbLoading.isVisible = state.isLoading
            binding.tvMessageError.text = state.messageError

            if(state.userTypeSelected.userBuyerSelected){
                binding.imageViewBuyer.setBackgroundResource(R.drawable.shape_type_user)
                binding.imageViewBuyer.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                binding.tvNameBuyer.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            } else {
                binding.imageViewBuyer.setBackgroundResource(R.drawable.shape_tupe_user_transparent)
                binding.imageViewBuyer.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                binding.tvNameBuyer.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            if(state.userTypeSelected.userProviderSelected){
                binding.imageViewProvider.setBackgroundResource(R.drawable.shape_type_user)
                binding.imageViewProvider.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                binding.tvNameProvider.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            } else {
                binding.imageViewProvider.setBackgroundResource(R.drawable.shape_tupe_user_transparent)
                binding.imageViewProvider.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                binding.tvNameProvider.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        viewModel.eventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is RegisterEvent.SuccessRegister -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    findNavController ().popBackStack()
                }
                RegisterEvent.EnterCnpj -> {
                    binding.edtInputCnpj.addTextChangedListener(Maks.insert(binding.edtInputCnpj))
                    Maks.insert(binding.edtInputCnpj)
                }
            }
        }
    }

}