package com.millionpugs.balance.service

import com.millionpugs.account.exception.AccountNotFoundException
import com.millionpugs.account.service.AccountService
import com.millionpugs.api.nbp.client.NbpClient
import com.millionpugs.api.nbp.dto.NbpConversionResponse
import com.millionpugs.api.nbp.dto.NbpRateResponse
import com.millionpugs.api.nbp.exception.AmbiguousResponseException
import com.millionpugs.user.entity.Account
import com.millionpugs.user.entity.User
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.UUID
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


internal class BalanceServiceImplTest {
    private val expectedUserId = UUID.randomUUID()

    private val accountService: AccountService = mock()
    private val nbpClient: NbpClient = mock()
    private val balanceService: BalanceService = BalanceServiceImpl(accountService, nbpClient)


    @ParameterizedTest()
    @MethodSource("validBalances")
    fun `getBalanceInUSD should return valid response`(balance: BigDecimal, dollarPrice: BigDecimal, expected: BigDecimal) = runTest {
        whenever(accountService.getUserById(expectedUserId)).thenReturn(getUser(balance))
        whenever(nbpClient.getUSDPrice()).thenReturn(getNbpResponse(dollarPrice))

        val response = assertNotNull(balanceService.getBalanceInUSD(expectedUserId))

        assertEquals("USD", response.currency)
        assertEquals(expected, response.balance)
    }

    @Test
    fun `getBalanceInUSD should throw AccountNotFoundException if user does not have bank account`() = runTest {
        whenever(accountService.getUserById(expectedUserId)).thenReturn(null)

        val exception= assertThrows<AccountNotFoundException> {
            balanceService.getBalanceInUSD(expectedUserId)
        }
        assertEquals(String.format("Account for userId=%s not found", expectedUserId), exception.message)
    }

    @ParameterizedTest()
    @MethodSource("invalidNBPResponses")
    fun `getBalanceInUSD should throw AmbiguousResponseException if NBP Api returned incorrect response`(nbpConversionResponse: NbpConversionResponse, expectedExceptionMessage: String) = runTest {
        whenever(accountService.getUserById(expectedUserId)).thenReturn(getUser(BigDecimal.ZERO))
        whenever(nbpClient.getUSDPrice()).thenReturn(nbpConversionResponse)

        val exception = assertThrows<AmbiguousResponseException> {
            balanceService.getBalanceInUSD(expectedUserId)
        }
        assertEquals(expectedExceptionMessage, exception.message)
    }

    companion object {
        @JvmStatic
        fun validBalances(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(10_000.0.toBigDecimal(), 4.0312.toBigDecimal(), 2480.65.toBigDecimal()),
                Arguments.of(1.0.toBigDecimal(), 4.01.toBigDecimal(), 0.25.toBigDecimal()),
                Arguments.of(567.21.toBigDecimal(), 3.0.toBigDecimal(), 189.07.toBigDecimal()),
                Arguments.of(BigDecimal.ZERO, 2.67998.toBigDecimal(), BigDecimal.ZERO.setScale(2)),
                Arguments.of(-BigDecimal.ZERO, 2.67998.toBigDecimal(), BigDecimal.ZERO.setScale(2)),
                Arguments.of((-5_677.87).toBigDecimal(), 2.9.toBigDecimal(), (-1957.89).toBigDecimal()),
                Arguments.of((-0.01).toBigDecimal(), 2.887.toBigDecimal(), BigDecimal.ZERO.setScale(2)),
            )
        }

        @JvmStatic
        fun invalidNBPResponses(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(NbpConversionResponse("USD", emptyList()), "NBP API returned no conversion rates"),
                Arguments.of(NbpConversionResponse("USD", listOf(
                    NbpRateResponse(BigDecimal.ZERO),
                    NbpRateResponse(BigDecimal.ZERO)
                )), "NBP API returned more than one mid conversion rate"),
            )
        }
    }

    private fun getUser(balance: BigDecimal) = User(expectedUserId, "John", "Smith", Account(UUID.randomUUID(), balance))

    private fun getNbpResponse(dollarPrice: BigDecimal) = NbpConversionResponse("USD", listOf(NbpRateResponse(dollarPrice)))
}
