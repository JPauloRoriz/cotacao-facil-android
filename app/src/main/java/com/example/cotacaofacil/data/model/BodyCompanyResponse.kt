package com.example.cotacaofacil.data.model

import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

data class BodyCompanyResponse(
//    val atividade_principal: MainActivityResponse,
    var nome : String? = null,
    var uf : String? = null,
    var situacao: String? = null,
    var municipio : String? = null,
    var fantasia : String? = null,
    var abertura : String? = null,
    var status : String? = null,
    var cnpj : String? = null,
    var typeUser : UserTypeSelected? = null
)

//data class MainActivityResponse(
//    val code: String,
//    val text : String
//)