import java.util.*

plugins {
  java
  id("org.springframework.boot") version "3.4.5"
  id("io.spring.dependency-management") version "1.1.7"
  jacoco
  id("org.sonarqube") version "6.1.0.5360"
  id("com.github.ben-manes.versions") version "0.52.0"
  id("org.openapi.generator") version "7.13.0"
  id("org.ajoberstar.grgit") version "5.3.0"
  id("com.gorylenko.gradle-git-properties") version "2.5.0"
}

group = "it.gov.pagopa.payhub"
version = "0.0.1"
description = "p4pa-pu-bff"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

val springDocOpenApiVersion = "2.8.6"
val openApiToolsVersion = "0.2.6"
val micrometerVersion = "1.4.6"
val caffeineVersion = "3.2.0"
val httpClientVersion = "5.4.4"
val mapStructVersion = "1.6.3"

val wiremockVersion = "3.13.0"
val wiremockSpringBootVersion = "3.10.0"
val podamVersion = "8.0.2.RELEASE"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
  implementation("org.springframework.data:spring-data-commons")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
  implementation("org.apache.httpcomponents.client5:httpclient5:$httpClientVersion")
  implementation ("org.mapstruct:mapstruct:${mapStructVersion}")


  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")
  testAnnotationProcessor("org.projectlombok:lombok")
  testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")

  //	Testing
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.mockito:mockito-core")
  testImplementation("org.projectlombok:lombok")
  testImplementation("org.wiremock:wiremock-standalone:$wiremockVersion")
  testImplementation("org.wiremock.integrations:wiremock-spring-boot:$wiremockSpringBootVersion")
  testImplementation("uk.co.jemos.podam:podam:$podamVersion")
}

tasks.withType<Test> {
  useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
  mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}
tasks {
  test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
  }
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required = true
  }
}

val projectInfo = mapOf(
  "artifactId" to project.name,
  "version" to project.version
)

tasks {
  val processResources by getting(ProcessResources::class) {
    filesMatching("**/application.yml") {
      expand(projectInfo)
    }
  }
}

configurations {
  compileClasspath {
    resolutionStrategy.activateDependencyLocking()
  }
}

tasks.compileJava {
  dependsOn("dependenciesBuild")
}

tasks.register("dependenciesBuild") {
  group = "AutomaticallyGeneratedCode"
  description = "grouping all together automatically generate code tasks"

  dependsOn(
    "openApiGenerateBFF",
    "openApiGenerateP4PAAUTH",
    "openApiGenerateORGANIZATION",
    "openApiGenerateDEBTPOSITIONS",
    "openApiGeneratePROCESSEXECUTIONS",
    "openApiGenerateCLASSIFICATION",
    "openApiGeneratePAGOPAPAYMENTS"
  )
}

configure<SourceSetContainer> {
  named("main") {
    java.srcDir("$projectDir/build/generated/src/main/java")
  }
}

springBoot {
  buildInfo()
  mainClass.value("it.gov.pagopa.pu.bff.PiattaformaUnitariaBffApplication")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateBFF") {
  group = "openapi"
  description = "description"

  generatorName.set("spring")
  inputSpec.set("$rootDir/openapi/p4pa-pu-bff.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.bff.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.bff.dto.generated")
  typeMappings.set(mapOf(
    "AccessToken" to "it.gov.pagopa.pu.auth.dto.generated.AccessToken",
    "DebtPositionType" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType",
    "IngestionFlowFileType" to "it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.IngestionFlowFileTypeEnum",
    "IngestionFlowFileStatus" to "it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFileStatus",
    "ExportFileType" to "it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum",
    "ExportFileStatus" to "it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus",
    "PaidExportFileRequestDTO" to "it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO",
    "ClassificationsExportFileRequestDTO" to "it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO",
    "PaymentsReportingExportFileRequestDTO" to "it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO",
    "ReceiptsArchivingExportFileRequestDTO" to "it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO",
    "PaidExportFileType" to "it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO.ExportFileTypeEnum",
    "ReceiptsArchivingExportFileType" to "it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO.ExportFileTypeEnum",
    "ReceiptView" to "it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView",
    "ReceiptOriginType" to "it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType",
    "DebtPositionTypeOrg" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg",
    "DebtPositionTypeOrgWithCount" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgWithCount",
    "InstallmentView" to "it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView",
    "PaymentsReportingView" to "it.gov.pagopa.pu.classification.dto.generated.PaymentsReportingView",
    "TreasuryView" to "it.gov.pagopa.pu.classification.dto.generated.TreasuryView",
    "Treasury" to "it.gov.pagopa.pu.classification.dto.generated.Treasury",
    "ClassificationDetailViewDTO" to "it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO",
    "DebtPositionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO",
    "DebtPositionView" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView",
    "DebtPositionStatus" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus",
    "PaymentOptionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO",
    "PersonDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO",
    "InstallmentDetailDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO",
    "InstallmentStatus" to "it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus",
    "PaymentsReporting" to "it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting",
    "Transfer" to "it.gov.pagopa.pu.debtpositions.dto.generated.Transfer",
    "UserInfo" to "it.gov.pagopa.pu.auth.dto.generated.UserInfo",
    "DebtPositionTypeResponseBody" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType",
    "PagedTreasuredClassification" to "it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification",
    "ClassificationsEnum" to "it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum",
    "DebtPositionOrigin" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin",
    "LocalDateInterval" to "it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter"
  ))
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "useBeanValidation" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
}

var targetEnv = when (Objects.requireNonNullElse(System.getProperty("targetBranch"), grgit.branch.current().name)) {
  "uat" -> "uat"
  "main" -> "main"
  else -> "develop"
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateP4PAAUTH") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-auth/refs/heads/$targetEnv/openapi/p4pa-auth.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.auth.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.auth.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "serializationLibrary" to "jackson",
    "useBeanValidation" to "true",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateORGANIZATION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-organization/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.organization.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.organization.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateDEBTPOSITIONS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-debt-positions/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.debtpositions.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.debtpositions.dto.generated")
  typeMappings.set(mapOf(
    "LocalDateTime" to "java.time.LocalDateTime"
  ))
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  additionalProperties.set(mapOf(
    "removeEnumValuePrefix" to "false"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePROCESSEXECUTIONS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-process-executions/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.processexecutions.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.processexecutions.dto.generated")
  typeMappings.set(mapOf(
    "LocalDateTime" to "java.time.LocalDateTime"
  ))
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateCLASSIFICATION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-classification/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.classification.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.classification.dto.generated")
  typeMappings.set(mapOf(
    "LocalDateTime" to "java.time.LocalDateTime"
  ))
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePAGOPAPAYMENTS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-pagopa-payments/refs/heads/$targetEnv/openapi/p4pa-pagopa-payments.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.pagopapayments.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.pagopapayments.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  typeMappings.set(mapOf(
    "string+binary" to "Resource",
    "DebtPositionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO"
  ))
  importMappings.set(mapOf(
    "Resource" to "org.springframework.core.io.Resource"
  ))
  library.set("resttemplate")
}
