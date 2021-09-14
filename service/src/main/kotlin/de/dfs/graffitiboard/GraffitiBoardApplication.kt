package de.dfs.graffitiboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class GraffitiBoardApplication

fun main(args: Array<String>) {
    runApplication<GraffitiBoardApplication>(*args)
}
