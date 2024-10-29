package com.example.letsdart2.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginDto(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("password")
    val password: String,
)
