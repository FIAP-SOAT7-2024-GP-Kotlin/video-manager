package io.github.soat7.videomanager.mock

import io.mockk.every
import io.mockk.mockk
import io.nats.client.Connection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NatsMock {

    @Bean
    fun connection() = mockedConnection()

    private fun mockedConnection(): Connection {
        return mockk(
            relaxed = true,
            relaxUnitFun = true
        ) {
            every { publish(any(), any()) } returns mockk()
            every { subscribe(any(), any()) } returns mockk()
            every { close() } returns mockk()
        }
    }
}
