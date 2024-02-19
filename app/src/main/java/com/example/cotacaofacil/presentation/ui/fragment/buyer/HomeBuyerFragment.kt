package com.example.cotacaofacil.presentation.ui.fragment.buyer

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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentHomeBuyerBinding
import com.example.cotacaofacil.presentation.ui.activity.StockBuyerActivity
import com.example.cotacaofacil.presentation.ui.activity.StockBuyerActivity.Companion.ADD_PRODUCT_BOTTOM_SHEET
import com.example.cotacaofacil.presentation.ui.dialog.AddProductBottomSheetDialogFragment
import com.example.cotacaofacil.presentation.util.BottomNavigationListener
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.HomeBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeBuyerFragment() : Fragment() {
    private val viewModel by viewModel<HomeBuyerViewModel>()
    private lateinit var binding: FragmentHomeBuyerBinding
    private var backPressedOnce: Boolean = false
    private var bottomNavigationListener: BottomNavigationListener? = null

    private var fragmentManager: FragmentManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(activity: Activity) {
        fragmentManager = this.childFragmentManager
        super.onAttach(activity)
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
        viewModel.homeBuyerEventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                HomeBuyerEvent.ErrorLoadListProducts -> {
                    Toast.makeText(requireContext(), context?.getString(R.string.message_error_impossible_load_products_stock), Toast.LENGTH_SHORT)
                        .show()
                }
                is HomeBuyerEvent.ListEmptyProducts -> {
                    Toast.makeText(requireContext(), context?.getString(R.string.products_empty_toast_add), Toast.LENGTH_SHORT).show()
                    fragmentManager?.let {
                        AddProductBottomSheetDialogFragment.newInstance(
                            cnpjUser = event.user?.cnpj,
                            productModel = null,
                            {}
                        ) {}.show(it, ADD_PRODUCT_BOTTOM_SHEET)
                    }
                }
                is HomeBuyerEvent.SuccessListProducts -> {
                    val intent = Intent(requireContext(), StockBuyerActivity::class.java)
                    intent.putExtra(StockBuyerActivity.USER, event.user)
                    startActivityForResult(intent, REQUEST_QUANTITY_PRODUCTS)
                }
                HomeBuyerEvent.AskAgain -> {
                    backPressedOnce = true
                    Toast.makeText(requireContext(), getString(R.string.click_again_to_close), Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({ backPressedOnce = false }, 2000)
                }
                HomeBuyerEvent.FinishApp -> {
                    requireActivity().finishAffinity()
                }
                HomeBuyerEvent.ClickPartner -> {
                    openFragment(R.id.partnerBuyerFragment)
                }
                is HomeBuyerEvent.ErrorLoadInformation -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                HomeBuyerEvent.Logout -> activity?.finish()
                HomeBuyerEvent.ClickCardPrices -> openFragment(R.id.priceBuyerFragment)
            }
        }

        viewModel.homeBuyerStateLiveData.observe(viewLifecycleOwner) { state ->
            binding.textViewCorporateName.text = state.nameCorporation
            binding.textViewFantasyName.text = state.nameFantasy
            binding.textViewFone.text = state.fone
            binding.textViewCnpj.text = state.cnpj
            binding.textViewNumberStock.text = state.quantityProducts
            binding.textViewNumberOpenPrice.text = state.quantityPrice
            binding.textViewEmail.text = state.email
        }
    }

    private fun setupListeners() {
        binding.textViewLogoff.setOnClickListener {
            viewModel.tapOnLogout()
        }
        binding.cardViewPrice.setOnClickListener {
            viewModel.tapOnCardPrices()
        }

        binding.constraintLayoutProvider.setOnClickListener {
            viewModel.tapOnPartner()
        }

        binding.constraintLayoutInformationCorporation.setOnClickListener {
            Toast.makeText(requireContext(), "informações pessoais", Toast.LENGTH_SHORT).show()
        }

        binding.cardViewStock.setOnClickListener {
            viewModel.tapOnCardStock()
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

    private fun openFragment(idFragment: Int) {
        bottomNavigationListener?.onChangeFragmentBottomNavigation(idFragment)
    }

    companion object {
        private const val REQUEST_QUANTITY_PRODUCTS = 222
    }
}
