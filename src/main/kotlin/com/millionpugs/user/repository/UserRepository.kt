package com.millionpugs.user.repository

import com.millionpugs.user.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID>
