package com.example.letsdart2.auth

import com.example.letsdart2.user.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val userService: UserService
) {

    @ResponseBody
    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<*>? {
        return try {
            val token = userService.logInUser(loginDto)
            ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build<Unit>()
        } catch (e: BadCredentialsException) {
            ResponseEntity.badRequest().body("Invalid username or password")
        }
    }

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<*>? {
        return try {
            val token: String = userService.registerUser(registerDto)
            ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.AUTHORIZATION, token).build<Unit>()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }
    }
}