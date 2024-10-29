package com.example.letsdart2.user

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: Long, @RequestBody newUser: User,): ResponseEntity<Unit> {
        return try {
             ResponseEntity.ok(userService.updateUser(userId, newUser))
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}