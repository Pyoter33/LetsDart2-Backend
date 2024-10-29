package com.example.letsdart2.user

import com.example.letsdart2.auth.LoginDto
import com.example.letsdart2.auth.RegisterDto
import com.example.letsdart2.security.JwtUtil
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun logInUser(loginDto: LoginDto): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.email,
                loginDto.password
            )
        )
        return jwtUtil.generateToken(authentication.name)
    }

    fun registerUser(registerDto: RegisterDto): String {
        require(!userRepository.existsByEmail(registerDto.email)) { "Account with given email already exists" }

        val password = Password(passwordEncoder.encode(registerDto.password))
        val newUser = User(
            registerDto.email,
            password,
            registerDto.userName
        )
        password.user = newUser
        userRepository.save(newUser)
        return jwtUtil.generateToken(registerDto.email)
    }

    fun updateUser(userId: Long, newUser: User) {
        userRepository.findByIdOrNull(userId)?.let {
            userRepository.save(
                it.apply {
                    email = newUser.email
                }
            )
        } ?: run {
            throw NotFoundException()
        }
    }
}