package io.github.soat7.videomanager.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader
import org.springframework.http.codec.multipart.MultipartHttpMessageReader
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebConfiguration(
    private val objectMapper: ObjectMapper
) : WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val partReader = DefaultPartHttpMessageReader()
        partReader.setMaxParts(3)
        partReader.setMaxDiskUsagePerPart(10L * 4000000L) // To 10MB.
        partReader.isEnableLoggingRequestDetails = true

        val multiPartReader = MultipartHttpMessageReader(partReader)
        multiPartReader.isEnableLoggingRequestDetails = true

        configurer.defaultCodecs().multipartReader(multiPartReader)
        configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
        configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
    }
}
