package com.example.cotacaofacil.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.example.cotacaofacil.data.helper.BodyCompanyHelper
import com.example.cotacaofacil.data.helper.SpinnerListHelper
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.data.repository.bodyCompany.BodyCompanyRepositoryImpl
import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.repository.history.HistoryRepositoryImpl
import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.data.repository.partner.PartnerRepositoryImpl
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.data.repository.product.ProductRepositoryImpl
import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.data.repository.user.UserRepositoryImpl
import com.example.cotacaofacil.data.repository.user.contract.UserRepository
import com.example.cotacaofacil.data.service.Date.DateCurrentImpl
import com.example.cotacaofacil.data.service.Date.contract.DateCurrent
import com.example.cotacaofacil.data.service.cnpj.BodyCompanyServiceImpl
import com.example.cotacaofacil.data.service.cnpj.CnpjServiceImpl
import com.example.cotacaofacil.data.service.cnpj.contract.BodyCompanyService
import com.example.cotacaofacil.data.service.history.HistoryServiceImpl
import com.example.cotacaofacil.data.service.history.contract.HistoryService
import com.example.cotacaofacil.data.service.partner.PartnerServiceImpl
import com.example.cotacaofacil.data.service.partner.contract.PartnerService
import com.example.cotacaofacil.data.service.product.ProductServiceImpl
import com.example.cotacaofacil.data.service.product.contract.ProductService
import com.example.cotacaofacil.data.service.settings.retrofitConfig
import com.example.cotacaofacil.data.service.user.UserFirebaseService
import com.example.cotacaofacil.data.service.user.contract.UserService
import com.example.cotacaofacil.data.sharedPreferences.SharedPreferencesHelper
import com.example.cotacaofacil.domain.usecase.history.DeleteHistoricUseCaseImpl
import com.example.cotacaofacil.domain.usecase.history.GetAllItemHistoryUseCaseImpl
import com.example.cotacaofacil.domain.usecase.history.contract.DeleteHistoricUseCase
import com.example.cotacaofacil.domain.usecase.history.contract.GetAllItemHistoryUseCase
import com.example.cotacaofacil.domain.usecase.home.GetBodyCompanyModelUseCaseImpl
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase
import com.example.cotacaofacil.domain.usecase.login.LoginUseCaseImpl
import com.example.cotacaofacil.domain.usecase.login.contract.LoginUseCase
import com.example.cotacaofacil.domain.usecase.partner.*
import com.example.cotacaofacil.domain.usecase.partner.contract.*
import com.example.cotacaofacil.domain.usecase.product.*
import com.example.cotacaofacil.domain.usecase.product.contract.*
import com.example.cotacaofacil.domain.usecase.register.RegisterUseCaseImpl
import com.example.cotacaofacil.domain.usecase.register.contract.RegisterUseCase
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.HomeBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.history.HistoryViewModel
import com.example.cotacaofacil.presentation.viewmodel.login.LoginViewModel
import com.example.cotacaofacil.presentation.viewmodel.partner.PartnerViewModel
import com.example.cotacaofacil.presentation.viewmodel.product.AddProductViewModel
import com.example.cotacaofacil.presentation.viewmodel.product.StockBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.register.RegisterViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val appModule = module {

    //viewModel
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get(), get()) }
    viewModel { PartnerViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AddProductViewModel(get(), get(), get(), get(), get()) }
    viewModel { StockBuyerViewModel(get(), get(), get(), get(), get()) }
    viewModel { HomeBuyerViewModel(get(), get(), get(), get(), get(), get()) }


    //useCase
    factory<RegisterUseCase> { RegisterUseCaseImpl(get(), get(), get()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get(), get()) }
    factory<ValidationCnpjUseCase> { ValidationCnpjUseCaseImpl(get()) }
    factory<GetAllPartnerModelUseCase> { GetAllPartnerModelUseCaseImpl(get()) }
    factory<AddRequestPartnerUseCase> { AddRequestRequestPartnerUseCaseImpl(get(), get(), get(), get()) }
    factory<RejectRequestPartnerUseCase> { RejectRequestPartnerUseCaseImpl(get(), get(), get(), get()) }
    factory<AcceptRequestPartnerUseCase> { AcceptRequestPartnerUseCaseImpl(get(), get(), get(), get()) }
    factory<GetAllItemHistoryUseCase> { GetAllItemHistoryUseCaseImpl(get()) }
    factory<GetAllListSpinnerOptionsUseCase> { GetAllListSpinnerOptionsUseCaseImpl(get()) }
    factory<SaveProductionUseCase> { SaveProductionUseCaseImpl(get()) }
    factory<GetAllProductsUseCase> { GetAllProductsUseCaseImpl(get()) }
    factory<GetBodyCompanyModelUseCase> { GetBodyCompanyModelUseCaseImpl(get()) }
    factory<DeleteHistoricUseCase> { DeleteHistoricUseCaseImpl(get()) }
    factory<ChangeFavoriteProductUseCase> { ChangeFavoriteProductUseCaseImpl(get()) }
    factory<EditProductUseCase> { EditProductUseCaseImpl(get()) }
    factory<DeleteProductUseCase> { DeleteProductUseCaseImpl(get()) }


    //repository
    factory<UserRepository> { UserRepositoryImpl(get()) }
    factory<BodyCompanyRepository> { BodyCompanyRepositoryImpl(get(), get()) }
    factory<PartnerRepository> { PartnerRepositoryImpl(get()) }
    factory<HistoryRepository> { HistoryRepositoryImpl(get()) }
    factory<ProductRepository> { ProductRepositoryImpl(get(), get(), get(), get()) }


    //service
    single<UserService> { UserFirebaseService(get(), get()) }
    single<PartnerService> { PartnerServiceImpl(get()) }
    single<BodyCompanyService> { BodyCompanyServiceImpl(get()) }
    single<HistoryService> { HistoryServiceImpl(get(), get()) }
    single<ProductService> { ProductServiceImpl(get()) }
    single<DateCurrent> { DateCurrentImpl() }

    //helper
    single { SpinnerListHelper() }
    single { UserHelper() }
    single { BodyCompanyHelper() }
    single { SharedPreferencesHelper(get(), get()) }

    single { retrofitConfig }
    single { get<Retrofit>().create(CnpjServiceImpl::class.java) }
    single { Firebase.firestore }
    single { Firebase.auth }
    single<SharedPreferences> { androidContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    single {
        val context: Context = androidContext()
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            context.packageName + "_preferences",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }
}