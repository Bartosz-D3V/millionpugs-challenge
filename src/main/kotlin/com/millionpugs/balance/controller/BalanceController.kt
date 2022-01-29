package com.millionpugs.balance.controller

import com.millionpugs.balance.dto.AccountBalanceResponse
import com.millionpugs.balance.service.BalanceService
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(path = ["/balance/"])
class BalanceController(val balanceService: BalanceService) {
    @GetMapping(path = ["/users/{id}/"], produces = ["application/json"])
    fun findAccountBalance(@PathVariable id: UUID): AccountBalanceResponse= runBlocking {
        balanceService.getBalanceInUSD(id)
    }
}
