package io.github.soat7.videomanager.thirdparty.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DigitalOceanConfig(
    @Value("\${digital-ocean.spaces.credentials.access-key}") private val accessKey: String,
    @Value("\${digital-ocean.spaces.credentials.secret-key}") private val secretKey: String,
    @Value("\${digital-ocean.spaces.endpoint}") private val doEndpoint: String,
    @Value("\${digital-ocean.spaces.region}") private val region: String
) {

    @Bean
    fun amazonS3Client(): AmazonS3 {
        val credentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(doEndpoint, region))
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }
}
