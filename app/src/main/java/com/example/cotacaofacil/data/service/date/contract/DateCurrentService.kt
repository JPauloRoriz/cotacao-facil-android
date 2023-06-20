package com.example.cotacaofacil.data.service.date.contract

interface DateCurrentService {
  suspend fun invoke() : Result<Long>
}