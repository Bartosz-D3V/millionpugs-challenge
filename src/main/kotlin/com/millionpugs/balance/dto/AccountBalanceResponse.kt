package com.millionpugs.balance.dto

import java.math.BigDecimal

data class AccountBalanceResponse(val fullName: String, val balance: BigDecimal, val currency: String)
