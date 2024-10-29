package com.example.letsdart2.user

import jakarta.persistence.*

@Entity(name = "passwords")
class Password(
    @Column(name = "password")
    var password: String
) {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    lateinit var user: User
}
