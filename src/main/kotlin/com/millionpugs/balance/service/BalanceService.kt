package com.millionpugs.balance.service

import com.millionpugs.balance.dto.AccountBalanceResponse
import java.util.UUID

interface BalanceService {
    fun getBalanceInUSD(userId: UUID): AccountBalanceResponse?
}