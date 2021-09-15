package de.dfs.graffitiboard.message.api

import de.dfs.graffitiboard.message.service.Message
import org.springframework.stereotype.Component

@Component
class MessageDtoMapper {

    fun mapToMessage(messageDto: MessageDto) = Message(author = messageDto.author, message = messageDto.message)

    fun mapToReadDto(message: Message) = MessageReadDto(message.author, message.message, message.createdAt!!)
}
