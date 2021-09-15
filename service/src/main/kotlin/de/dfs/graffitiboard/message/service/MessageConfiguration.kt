package de.dfs.graffitiboard.message.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.time.Duration

@Configuration
class MessageConfiguration {

    @Bean
    fun messageSink(): Sinks.Many<Message> {
        return Sinks.many().replay().limit(Duration.ofHours(1L))
    }

    @Bean
    fun messageFlux(notificationSink: Sinks.Many<Message>): Flux<Message> = notificationSink.asFlux()
}
