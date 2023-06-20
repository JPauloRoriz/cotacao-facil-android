package com.example.cotacaofacil.presentation.ui.fragment.provider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentHomeProviderBinding
import com.example.cotacaofacil.presentation.util.BottomNavigationListener
import com.example.cotacaofacil.presentation.viewmodel.provider.home.HomeProviderViewModel
import com.example.cotacaofacil.presentation.viewmodel.provider.home.contract.HomeProviderEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeProviderFragment : Fragment() {
    private val viewModel by viewModel<HomeProviderViewModel>()
    private lateinit var binding: FragmentHomeProviderBinding

    private var backPressedOnce: Boolean = false
    private var bottomNavigationListener: BottomNavigationListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeProviderBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw RuntimeException("$context must implement BottomNavigationListener")
        }
    }

    private fun setupObservers() {
        viewModel.homeProviderEventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                HomeProviderEvent.AskAgain -> {
                    backPressedOnce = true
                    Toast.makeText(requireContext(), getString(R.string.click_again_to_close), Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({ backPressedOnce = false }, 2000)
                }
                HomeProviderEvent.FinishApp -> {
                    requireActivity().finishAffinity()
                }
                HomeProviderEvent.ClickPartner -> {
                    bottomNavigationListener?.onChangeFragmentBottomNavigation(R.id.partnerBuyerFragment)
                }
                is HomeProviderEvent.ErrorLoadInformation -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()

                HomeProviderEvent.Logout -> activity?.finish()
            }
        }

        viewModel.homeProviderStateLiveData.observe(viewLifecycleOwner) { state ->
            binding.textViewCorporateName.text = state.nameCorporation
            binding.textViewFantasyName.text = state.nameFantasy
            binding.textViewFone.text = state.fone
            binding.textViewCnpj.text = state.cnpj
            binding.textViewEmail.text = state.email
        }
    }

    private fun setupListeners() {
        binding.textViewLogoff.setOnClickListener {
            viewModel.tapOnLogout()
        }

        binding.constraintLayoutProvider.setOnClickListener {
            viewModel.tapOnPartner()
        }

        binding.constraintLayoutInformationCorporation.setOnClickListener {
            Toast.makeText(requireContext(), "informações pessoais", Toast.LENGTH_SHORT).show()
        }

        binding.arrow.setOnClickListener {
            viewModel.tapOnArrowBack(backPressedOnce)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.tapOnArrowBack(backPressedOnce)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_QUANTITY_PRODUCTS && resultCode == Activity.RESULT_OK) {
            lifecycleScope.launchWhenCreated {
                viewModel.loadDataUser()
            }
        }
    }

    companion object {
        private const val REQUEST_QUANTITY_PRODUCTS = 222
    }
}