package com.millionpugs.account.service

import com.millionpugs.user.entity.User
import java.util.UUID

interface AccountService {
    fun getUserById(id: UUID): User?
}
