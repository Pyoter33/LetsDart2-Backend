package com.example.letsdart2.security

import com.example.letsdart2.user.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository): UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val appUser = userRepository.findByEmail(email)
        return User(email, appUser.userPassword.password, emptyList())
    }
}