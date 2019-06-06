package com.affinitycrypto.desking

import com.affinitycrypto.desking.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class DeskInformationApplication

fun main(args: Array<String>) {
    runApplication<DeskInformationApplication>(*args)
}
