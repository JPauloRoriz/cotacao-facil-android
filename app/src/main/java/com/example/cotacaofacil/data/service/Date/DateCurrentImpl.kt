package com.example.cotacaofacil.data.service.Date

import com.example.cotacaofacil.data.service.Date.contract.DateCurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import java.net.InetAddress

class DateCurrentImpl : DateCurrent {
    override suspend fun invoke(): Result<Long> {
        return Dispatchers.IO.invoke {
            val ntpClient = NTPUDPClient()
            ntpClient.defaultTimeout = 10000
            val timeServer = InetAddress.getByName("time.google.com")
            val timeInfo: TimeInfo = ntpClient.getTime(timeServer)
            val serverTime = timeInfo.message.transmitTimeStamp.time
            try {
                Result.success(serverTime)
            } catch (e: java.lang.Exception) {
                Result.failure(e)
            }
        }
    }
}