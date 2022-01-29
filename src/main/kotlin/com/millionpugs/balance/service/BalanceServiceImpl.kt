package com.millionpugs.balance.service

import com.millionpugs.account.exception.AccountNotFoundException
import com.millionpugs.account.service.AccountService
import com.millionpugs.api.nbp.client.NbpClient
import com.millionpugs.api.nbp.exception.AmbiguousResponseException
import com.millionpugs.balance.dto.AccountBalanceResponse
import com.millionpugs.user.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

private const val USD = "USD"

@Service
class BalanceServiceImpl(private val accountService: AccountService, private val nbpClient: NbpClient) :
    BalanceService {
    override suspend fun getBalanceInUSD(userId: UUID): AccountBalanceResponse {
        val userAccount = getUserAccount(userId)
        val dollarPrice = getDollarPrice()

        return AccountBalanceResponse(
            fullName = userAccount.getFullName(),
            balance = convertBalanceCurrency(userAccount, dollarPrice),
            currency = USD
        )
    }

    private suspend fun getUserAccount(userId: UUID): User {
        val accountDeferred = CoroutineScope(Dispatchers.IO).async(Dispatchers.IO, CoroutineStart.DEFAULT) {
            return@async accountService.getUserById(userId)
        }

        return accountDeferred.await()
            ?: throw AccountNotFoundException(String.format("Account for userId=%s not found", userId))
    }

    private suspend fun getDollarPrice(): BigDecimal {
        val conversionRateDeferred = CoroutineScope(Dispatchers.IO).async(Dispatchers.IO, CoroutineStart.DEFAULT) {
            return@async nbpClient.getUSDPrice()
        }

        val conversionRate = conversionRateDeferred.await() ?: throw RuntimeException("NBP API is not available")

        return when {
            conversionRate.rates.isEmpty() -> throw AmbiguousResponseException("NBP API returned no conversion rates")
            conversionRate.rates.size > 1 -> throw AmbiguousResponseException("NBP API returned more than one mid conversion rate")
            else -> conversionRate.rates.single().mid
        }
    }

    private fun convertBalanceCurrency(
        userAccount: User,
        dollarPrice: BigDecimal
    ): BigDecimal {
        val balanceInUsd = if (userAccount.account.balance != BigDecimal.ZERO) {
            userAccount.account.balance.setScale(2) / dollarPrice
        } else BigDecimal.ZERO

        return balanceInUsd.setScale(2, RoundingMode.HALF_UP)
    }
}
