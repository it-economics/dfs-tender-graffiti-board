package de.dfs.graffitiboard.message.persistence

import org.hibernate.annotations.CreationTimestamp
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "message")
data class MessageEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val author: String?,

    @Column(name = "message_body", length = 300, nullable = false)
    val message: String
) {
    @Column(name = "created_at", nullable = false, updatable = false)
    @field:CreationTimestamp
    lateinit var createdAt: ZonedDateTime
}
