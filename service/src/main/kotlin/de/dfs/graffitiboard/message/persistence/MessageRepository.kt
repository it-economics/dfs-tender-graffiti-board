package de.dfs.graffitiboard.message.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : CrudRepository<MessageEntity, Long> {

    fun findByOrderByCreatedAtDesc(): List<MessageEntity>
}
