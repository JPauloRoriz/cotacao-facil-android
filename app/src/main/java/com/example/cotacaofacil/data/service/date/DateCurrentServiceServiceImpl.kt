package com.example.cotacaofacil.data.service.date

import com.example.cotacaofacil.data.service.date.contract.DateCurrentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import java.net.InetAddress

class DateCurrentServiceServiceImpl : DateCurrentService {
    override suspend fun invoke(): Result<Long> {
        return Dispatchers.IO.invoke {
            val ntpClient = NTPUDPClient()
            ntpClient.defaultTimeout = 10000
            try {
                val timeServer = withContext(Dispatchers.IO) {
                    InetAddress.getByName("time.google.com")
                }
                val timeInfo: TimeInfo = ntpClient.getTime(timeServer)
                val serverTime = timeInfo.message.transmitTimeStamp.time
                Result.success(serverTime)
            } catch (e: java.lang.Exception) {
                Result.failure(e)
            }
        }
    }
}