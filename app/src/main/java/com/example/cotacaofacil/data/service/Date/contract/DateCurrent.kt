package com.example.cotacaofacil.data.service.Date.contract

interface DateCurrent {
  suspend fun invoke() : Result<Long>
}