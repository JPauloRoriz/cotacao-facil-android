package com.example.cotacaofacil.data.model

data class PartnerResponse(
    var cnpjUser : String,
    var approved : Boolean = false,
    var cnpjRequestingUser : String
)
