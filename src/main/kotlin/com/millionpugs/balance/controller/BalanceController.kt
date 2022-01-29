package com.millionpugs.balance.controller

import com.millionpugs.balance.dto.AccountBalanceResponse
import com.millionpugs.balance.service.BalanceService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
class BalanceController(val balanceService: BalanceService) {
    @GetMapping("users/{id}/account/")
    fun findAccountBalance(@PathVariable id: UUID): AccountBalanceResponse {
        return balanceService.getBalanceInUSD(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "User not found"
        )
    }
}