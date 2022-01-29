package com.millionpugs.account.service

import com.millionpugs.user.entity.User
import com.millionpugs.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountServiceImpl(val userRepository: UserRepository) : AccountService {
    override fun getUserById(id: UUID): User? {
        return userRepository.findById(id).orElse(null)
    }
}
