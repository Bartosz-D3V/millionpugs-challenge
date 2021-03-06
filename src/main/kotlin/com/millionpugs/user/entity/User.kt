package com.millionpugs.user.entity

import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid", nullable = false, length = 36)
    val id: UUID,

    @Column(name = "first_name", nullable = false, length = 200)
    val firstName: String,

    @Column(name = "last_name", nullable = false, length = 200)
    val lastName: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    val account: Account
) {
    fun getFullName() = String.format("%s %s", firstName, lastName)
}
