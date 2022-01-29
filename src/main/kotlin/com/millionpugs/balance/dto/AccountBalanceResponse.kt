package com.millionpugs.balance.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.millionpugs.common.serializer.MoneySerializer
import java.math.BigDecimal

data class AccountBalanceResponse(
    val fullName: String,

    @JsonSerialize(using = MoneySerializer::class)
    val balance: BigDecimal,

    val currency: String
)
