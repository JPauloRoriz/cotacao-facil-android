package com.example.cotacaofacil.data.service.cnpj

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CnpjServiceImpl {
    @GET("v1/cnpj/{cnpj}")
    suspend fun getCompanyByCnpj(@Path("cnpj") cnpj: String): BodyCompanyResponse

}
