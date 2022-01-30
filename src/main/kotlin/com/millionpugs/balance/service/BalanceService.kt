package com.millionpugs.balance.service

import com.millionpugs.account.exception.AccountNotFoundException
import com.millionpugs.api.nbp.exception.AmbiguousResponseException
import com.millionpugs.balance.dto.AccountBalanceResponse
import java.util.UUID

interface BalanceService {
    @Throws(AccountNotFoundException::class, AmbiguousResponseException::class)
    suspend fun getBalanceInUSD(userId: UUID): AccountBalanceResponse
}
