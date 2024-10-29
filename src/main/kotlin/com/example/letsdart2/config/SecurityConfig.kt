package com.example.letsdart2.config

import com.example.letsdart2.security.JwtAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(private val userDetailsService: UserDetailsService, private val jwtAuthorizationFilter: JwtAuthorizationFilter) {

    @Bean
    fun authenticationManager(http: HttpSecurity, passwordEncoder: PasswordEncoder): AuthenticationManager? {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
        return authenticationManagerBuilder.build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/auth/login").permitAll()
                auth.requestMatchers("/api/v1/auth/register").permitAll()
                auth.anyRequest().authenticated()
            }
            .exceptionHandling(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}