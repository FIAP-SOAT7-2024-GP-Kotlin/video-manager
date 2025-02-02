package io.github.soat7.videomanager.mock

import com.amazonaws.services.s3.AmazonS3
import io.mockk.every
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class AmazonS3Mocks {

    @Bean
    fun amazonS3() = mockedAmazonS3()

    private fun mockedAmazonS3(): AmazonS3 {
        return mockk(
            relaxed = true,
            relaxUnitFun = true
        ) {
            every { putObject(any(), any(), any<File>()) } returns mockk()
            every { putObject(any(), any(), any<String>()) } returns mockk()
            every { getObject(any(), any<String>()) } returns mockk()
            every { getObject(any(), any<File>()) } returns mockk()
            every { deleteObject(any(), any()) } returns mockk()
        }
    }
}
