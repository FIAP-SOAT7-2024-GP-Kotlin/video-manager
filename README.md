# Microserviço de Gerenciamento de Vídeo
Este é um microserviço que se integra com o Banco de Dados Relacional para persistir dados de usuário e de arquivos de vídeo. E também se integra com DigitalOcean Spaces para baixar arquivos
de vídeo e utiliza Kotlin e Spring Boot para realizar o gerenciamento.
## Requisitos
- Java JDK 21 ou superior
- Gradle
- Docker
- DigitalOcean Spaces SDK (doctl)
## Configuração
### Propriedades de Aplicação (`application.yml`)
```yaml
spring:
  application:
    name: video-manager
  codec:
    max-in-memory-size: 10MB
  jackson:
    default-property-inclusion: non_null
    deserialization:
      fail_on_unknown_properties: false
    serialization:
      fail_on_empty_beans: false
      write_dates_as_timestamps: false
    property-naming-strategy: SNAKE_CASE
  main:
    web-application-type: reactive
```
### Configuração de Autenticação (doctl)
- Instale o DigitalOcean CLI (doctl) seguindo as instruções no DigitalOcean Documentation.
- Configure a autenticação com sua conta do DigitalOcean.
## Build e Execução
### Compilação
> ./gradlew build
### Rodando em Desenvolvimento
> java -jar build/libs/video-manager-service-1.0-SNAPSHOT.jar --spring.profiles.active=dev
## Integração com Kubernetes
### Deploy no Kubernetes
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: video-manager

```
## Testes
### Execução de Testes Unitários
> ./gradlew test
