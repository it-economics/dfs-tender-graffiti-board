package de.dfs.graffitiboard.message.api

import de.dfs.graffitiboard.security.Roles
import de.dfs.graffitiboard.message.service.MessageService
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import javax.validation.Valid

@Validated
@RestController
@RequestMapping(
    value = ["/messages"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Secured(Roles.USER_ROLE)
class MessageController(
    private val messageService: MessageService,
    private val messageFlux: Flux<MessageReadDto>
) {

    @GetMapping
    fun getAll() = messageService.findAll()

    @PostMapping()
    fun post(@Valid @RequestBody message: MessageDto): ResponseEntity<MessageReadDto> =
        messageService.create(message)
            .let { ResponseEntity.ok(it) }

    @GetMapping(value = ["/subscribe"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun notifications(): ResponseEntity<Flux<MessageReadDto>> {
        val cacheControl = CacheControl.noCache().noTransform()

        return ResponseEntity.ok()
            .cacheControl(cacheControl)
            .body(messageFlux.map { it })
    }
}
