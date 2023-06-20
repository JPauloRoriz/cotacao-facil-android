package com.example.cotacaofacil.mock

import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.StatusIsMyPartner

class PartnerMock {
    companion object {

        val mock: MutableList<PartnerModel> = mutableListOf()
        fun listPartner(): MutableList<PartnerModel> {

            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "54725486598547",
                    false,
                    0,
                    StatusIsMyPartner.FALSE
                )
            )
            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "65877854425478",
                    false,
                    1,
                    StatusIsMyPartner.FALSE
                )
            )
            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "65585477879512",
                    false,
                    2,
                    StatusIsMyPartner.FALSE
                )
            )
            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "69993212524222",
                    false,
                    3,
                    StatusIsMyPartner.FALSE
                )
            )
            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "66666666666666",
                    false,
                    4,
                    StatusIsMyPartner.FALSE
                )
            )
            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "11111111111111",
                    false,
                    5,
                    StatusIsMyPartner.FALSE
                )
            )
            mock.add(
                PartnerModel(
                    "",
                    "",
                    "Comper aoihfoijfoiwejhfoiwehfioehfo",
                    "asffevqergqerverv",
                    "11221122112212",
                    false,
                    6,
                    StatusIsMyPartner.FALSE
                )
            )

            return mock
        }
    }
}