package com.example.letsdart2.user

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity(name = "users")
class User(
    @Column(name = "email", nullable = false)
    var email: String,
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var userPassword: Password,
    @Column(name = "user_name", nullable = false)
    var userName: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
