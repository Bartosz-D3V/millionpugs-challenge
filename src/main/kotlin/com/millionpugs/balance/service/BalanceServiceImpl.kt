package com.millionpugs.balance.service

import com.millionpugs.account.service.AccountService
import com.millionpugs.balance.dto.AccountBalanceResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BalanceServiceImpl(private val accountService: AccountService) : BalanceService {
    override fun getBalanceInUSD(userId: UUID): AccountBalanceResponse? {
        return accountService
            .getUserById(userId)
            ?.let {
                AccountBalanceResponse(
                    fullName = it.firstName + " " + it.lastName,
                    balance = it.account.balance,
                    currency = "PLN"
                )
            }
    }
}
