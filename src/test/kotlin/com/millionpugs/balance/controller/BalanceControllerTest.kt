package com.millionpugs.balance.controller

import com.millionpugs.account.exception.AccountNotFoundException
import com.millionpugs.api.nbp.exception.AmbiguousResponseException
import com.millionpugs.balance.dto.AccountBalanceResponse
import com.millionpugs.balance.service.BalanceService
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import kotlin.test.assertIs
import kotlin.test.assertNull

@SpringBootTest
@AutoConfigureMockMvc
class BalanceControllerTest {
    private val expectedUserId = UUID.randomUUID()

    @MockBean
    private lateinit var balanceService: BalanceService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findAccountBalance should return a valid response`() = runTest {
        whenever(balanceService.getBalanceInUSD(expectedUserId)).thenReturn(getBalance())

        val response = mockMvc
            .get(String.format("/balance/users/%s/", expectedUserId)) {
                accept = MediaType.APPLICATION_JSON
            }
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.fullName") {
                    value("John Smith")
                }
                jsonPath("$.balance") {
                    value("10000.00")
                }
                jsonPath("$.currency") {
                    value("USD")
                }
            }
            .andReturn()

        assertNull(response.resolvedException)
    }

    @Test
    fun `findAccountBalance should return 404 if account not found`() = runTest {
        whenever(balanceService.getBalanceInUSD(expectedUserId)).thenThrow(AccountNotFoundException(""))

        val response = mockMvc
            .get(String.format("/balance/users/%s/", expectedUserId)) {
                accept = MediaType.APPLICATION_JSON
            }
            .andExpect {
                status {
                    isNotFound()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.message") {
                    value("User does not exist or does not have an account with us.")
                }
            }
            .andReturn()

        assertIs<AccountNotFoundException>(response.resolvedException)
    }

    @Test
    fun `findAccountBalance should return 504 if NBP API failed`() = runTest {
        whenever(balanceService.getBalanceInUSD(expectedUserId)).thenThrow(AmbiguousResponseException(""))

        val response = mockMvc
            .get(String.format("/balance/users/%s/", expectedUserId)) {
                accept = MediaType.APPLICATION_JSON
            }
            .andExpect {
                status {
                    isInternalServerError()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.message") {
                    value("Service is not available now. Please try again later.")
                }
            }
            .andReturn()

        assertIs<AmbiguousResponseException>(response.resolvedException)
    }

    private fun getBalance() = AccountBalanceResponse(
        fullName = "John Smith",
        balance = 10_000.0.toBigDecimal().setScale(2),
        currency = "USD"
    )
}
