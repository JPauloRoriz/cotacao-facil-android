package com.example.cotacaofacil.data.repository.partner

import com.example.cotacaofacil.data.model.util.toPartnerModel
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.data.service.partner.contract.PartnerService
import com.example.cotacaofacil.domain.Extensions.Companion.convertCnpj
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.exception.UserNotFindException
import com.example.cotacaofacil.domain.mapper.mapperPartner
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import com.google.firebase.FirebaseNetworkException
import java.io.IOException

class PartnerRepositoryImpl(
    private val partnerService: PartnerService,
) : PartnerRepository {
    override suspend fun getPartnerByCnpj(
        userTypeSelected: UserTypeSelected,
        user: UserModel,
        cnpj: String
    ): Result<PartnerModel?> {
        user.id?.let { idUser ->
            partnerService.getBodyCompanyFirebaseByCnpj(userTypeSelected, idUser, cnpj)
                .onSuccess {
                    val partnerModel = it?.mapperPartner(it.cnpj, idUser)
                    partnerModel?.isMyPartner = partnerService.isMyPartner(user.cnpj, cnpj)
                    return Result.success(partnerModel)
                }
                .onFailure { error ->
                    return when (error) {
                        is IOException -> Result.failure(NoConnectionInternetException())
                        is FirebaseNetworkException -> Result.failure(NoConnectionInternetException())
                        is UserNotFindException -> Result.failure(UserNotFindException())
                        else -> Result.failure(DefaultException())
                    }
                }
        }
        return Result.failure(DefaultException())
    }


    override suspend fun getAllPartnerModel(
        userTypeSelected: UserTypeSelected,
        idUser: String,
        cnpjUser: String
    ): Result<MutableList<PartnerModel>> {
        val listPartners = partnerService.getPartnersByCnpj(cnpjUser).getOrNull() ?: emptyList()
        val listBodyPartnerModel: MutableList<PartnerModel> = mutableListOf()
        listPartners.forEach { patnerResponse ->
            partnerService.getBodyCompanyFirebaseByCnpj(userTypeSelected, "", patnerResponse.cnpjUser)
                .onSuccess {
                    val partnerModel = it?.cnpj?.let { it1 ->
                        it.toPartnerModel(idUser = idUser, cnpj = it1, date = patnerResponse.date)

                    }
                    partnerModel?.isMyPartner = partnerService.isMyPartner(cnpjUser, patnerResponse.cnpjUser)
                    partnerModel?.let { it1 -> listBodyPartnerModel.add(it1) }
                }
        }
        return Result.success(listBodyPartnerModel)
    }

    override suspend fun addRequestPartner(cnpj: String, partner: PartnerModel): Result<Unit?> {
        val partnerResponse = partner.mapperPartner(cnpj)
        return partnerService.addRequestPartnerResponse(cnpj, partnerResponse)
    }

    override suspend fun rejectPartner(cnpj: String, partnerModel: PartnerModel): Result<Boolean> {
        return partnerService.rejectPartner(
            cnpj,
            partnerModel.mapperPartner(partnerModel.cnpjCorporation.convertCnpj())
        )
    }

    override suspend fun acceptPartner(cnpj: String, partner: PartnerModel): Result<Boolean> {
        return partnerService.acceptPartner(
            cnpj,
            partner.mapperPartner(partner.cnpjCorporation.convertCnpj())
        )
    }


}