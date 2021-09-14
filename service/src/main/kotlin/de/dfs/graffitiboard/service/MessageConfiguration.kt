package de.dfs.graffitiboard.service

import de.dfs.graffitiboard.api.MessageReadDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.time.Duration

@Configuration
class MessageConfiguration {

    @Bean
    fun messageSink(): Sinks.Many<MessageReadDto> {
        return Sinks.many().replay().limit(Duration.ofHours(1L))
    }

    @Bean
    fun messageFlux(notificationSink: Sinks.Many<MessageReadDto>): Flux<MessageReadDto> = notificationSink.asFlux()
}
