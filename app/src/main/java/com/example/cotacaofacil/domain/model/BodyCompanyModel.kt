package com.example.cotacaofacil.domain.model

import com.google.gson.annotations.SerializedName

data class BodyCompanyModel(
//    val atividade_principal: MainActivityResponse = MainActivityResponse("", ""),
    val nome : String? = "",
    val uf : String? = "",
   @SerializedName("situacao")
    val situacao: String? = "",
    val municipio : String? = "",
    val fantasia : String? = "",
    val abertura : String? = "",
    val status : String? = "",
    var telefone : String? = "",
    @SerializedName("e-mail")
    var email : String? = "",
)