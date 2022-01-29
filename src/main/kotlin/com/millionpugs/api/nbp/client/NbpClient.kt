package com.millionpugs.api.nbp.client

import com.millionpugs.api.nbp.dto.NbpConversionResponse
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(value = "nbp", url ="\${nbp.url}",fallbackFactory= NbpClient.NbpClientFallback::class)
interface NbpClient {
    @RequestMapping(method = [RequestMethod.GET], value = ["/exchangerates/rates/a/USD/"], produces = ["application/json"])
    fun getUSDPrice(): NbpConversionResponse?

    @Component
    class NbpClientFallback : FallbackFactory<NbpClient> {
        override fun create(cause: Throwable) = object : NbpClient {

            override fun getUSDPrice(): NbpConversionResponse? {
               return null
            }
        }
    }
}