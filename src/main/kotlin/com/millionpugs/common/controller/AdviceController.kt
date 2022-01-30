package com.millionpugs.common.controller

import com.millionpugs.account.exception.AccountNotFoundException
import com.millionpugs.api.nbp.exception.AmbiguousResponseException
import com.millionpugs.common.dto.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class AdviceController : ResponseEntityExceptionHandler() {
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(req: HttpServletRequest, ex: Exception): ErrorInfo {
        return ErrorInfo("User does not exist or does not have an account with us.")
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AmbiguousResponseException::class)
    fun handleAmbiguousResponseException(req: HttpServletRequest, ex: Exception): ErrorInfo {
        return ErrorInfo("Service is not available now. Please try again later.")
    }
}
