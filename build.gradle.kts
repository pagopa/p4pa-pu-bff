import java.util.*
import com.github.jk1.license.render.*
import com.github.jk1.license.filter.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  java
  id("org.springframework.boot") version "4.0.6"
  id("io.spring.dependency-management") version "1.1.7"
  jacoco
  id("org.sonarqube") version "7.2.3.7755"
  id("com.github.ben-manes.versions") version "0.54.0"
  id("org.openapi.generator") version "7.22.0"
  id("org.ajoberstar.grgit") version "5.3.2"
  id("com.gorylenko.gradle-git-properties") version "2.5.7"
  id("com.github.jk1.dependency-license-report") version "3.1.2"
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
  compileClasspath {
    resolutionStrategy.activateDependencyLocking()
  }
}

licenseReport {
  renderers =
    arrayOf(XmlReportRenderer("third-party-libs.xml", "Back-End Libraries"))
  outputDir = "$projectDir/dependency-licenses"
  filters = arrayOf(SpdxLicenseBundleNormalizer())
}
tasks.classes {
  finalizedBy(tasks.generateLicenseReport)
}

repositories {
  mavenCentral()
}

val springDocOpenApiVersion = "3.0.3"
val janinoVersion = "3.1.12"
val openApiToolsVersion = "0.2.10"
val micrometerVersion = "1.6.5"
val caffeineVersion = "3.2.3"
val httpClientVersion = "5.6.1"
val httpCoreVersion = "5.4.2"
val mapStructVersion = "1.6.3"
val commonsLang3Version = "3.20.0"

val wiremockVersion = "3.13.2"
val wiremockSpringBootVersion = "4.2.1"
val podamVersion = "8.0.2.RELEASE"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
  implementation("org.springframework.boot:spring-boot-starter-restclient")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.data:spring-data-commons")
  implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion") {
    exclude(group = "org.apache.commons", module = "commons-lang3")
  }
  implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
  implementation("org.codehaus.janino:janino:$janinoVersion")
  implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
  implementation("org.springframework.data:spring-data-commons")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
  implementation("org.apache.httpcomponents.client5:httpclient5:$httpClientVersion")
  implementation("org.apache.httpcomponents.core5:httpcore5:$httpCoreVersion")
  implementation("org.mapstruct:mapstruct:${mapStructVersion}")


  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")
  testAnnotationProcessor("org.projectlombok:lombok")
  testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")

  //	Testing
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.springframework.boot:spring-boot-starter-security-test")
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
  jar {
      from("${rootProject.projectDir}") {
          include("LICENSE.md")
          into("META-INF")
      }
  }
  test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    testLogging.events = setOf(TestLogEvent.FAILED)
    testLogging.exceptionFormat = TestExceptionFormat.FULL
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
    "openApiGeneratePAGOPAPAYMENTS",
    "openApiGenerateREGISTRIES",
    "openApiGenerateWORKFLOWHUB"
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
  typeMappings.set(
    mapOf(
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
      "DebtPositionTypeOrgBalanceCostRequestDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostRequestDTO",
      "DebtPositionTypeOrgBalanceCostType" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType",
      "DebtPositionTypeOrgBalanceCost" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost",
      "DebtPositionTypeOrgWithCount" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgWithCount",
      "InstallmentView" to "it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentViewDTO",
      "PaymentsReportingView" to "it.gov.pagopa.pu.classification.dto.generated.PaymentsReportingView",
      "PaymentsReportingWithReceiptView" to "it.gov.pagopa.pu.classification.dto.generated.PaymentsReportingWithReceiptView",
      "TreasuryView" to "it.gov.pagopa.pu.classification.dto.generated.TreasuryView",
      "Treasury" to "it.gov.pagopa.pu.classification.dto.generated.Treasury",
      "ClassificationDetailDTO" to "it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO",
      "DebtPositionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO",
      "DebtPositionView" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView",
      "DebtPositionStatus" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus",
      "PaymentOptionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO",
      "PersonDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO",
      "PaymentOptionsExtendedDTO" to "it.gov.pagopa.pu.bff.dto.PaymentOptionsExtendedDTO",
      "InstallmentDetailDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO",
      "InstallmentStatus" to "it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus",
      "PaymentsReporting" to "it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting",
      "Transfer" to "it.gov.pagopa.pu.debtpositions.dto.generated.Transfer",
      "UserInfo" to "it.gov.pagopa.pu.auth.dto.generated.UserInfo",
      "UserOrganizationRoles" to "it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles",
      "DebtPositionTypeResponseBody" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType",
      "ClassificationsEnum" to "it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum",
      "DebtPositionOrigin" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin",
      "LocalDateInterval" to "it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter",
      "Taxonomy" to "it.gov.pagopa.pu.organization.dto.generated.Taxonomy",
      "DebtPositionRegistry" to "it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry",
      "InstallmentRegistry" to "it.gov.pagopa.pu.registries.dto.generated.InstallmentRegistry",
      "WorkflowCreatedDTO" to "it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO",
      "ManageDebtPositionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.ManageDebtPositionDTO",
      "OrgSilServiceType" to "it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType",
      "OrgSilServiceDTO" to "it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO",
      "AssessmentsRegistryStatus" to "it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus",
      "AssessmentsRegistry" to "it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry",
      "AssessmentsExtendedDTO" to "it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO",
      "AssessmentsStatusEnum" to "it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus",
      "AssessmentsDetail" to "it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail",
      "RegistryPagoPaEventType" to "it.gov.pagopa.pu.registries.dto.generated.RegistryPagoPaEventType",
      "PagoPaRegistry" to "it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistry",
      "SilRegistryDTO" to "it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO",
      "PagoPaRegistryDTO" to "it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO",
      "SilRegistry" to "it.gov.pagopa.pu.registries.dto.generated.SilRegistry",
      "RegistrySilEventType" to "it.gov.pagopa.pu.registries.dto.generated.RegistrySilEventType",
      "ScheduleEnum" to "it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum",
      "RegistrySilEventType" to "it.gov.pagopa.pu.registries.dto.generated.RegistrySilEventType",
      "Assessments" to "it.gov.pagopa.pu.classification.dto.generated.Assessments",
      "PagedClassificationPaidInstallmentsView" to "it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView",
      "CreateAssessmentsDetail" to "it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail",
      "OrgSilServiceView" to "it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceView",
      "OrgSilServiceDecryptedDTO" to "it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO",
      "RegistryOutcome" to "it.gov.pagopa.pu.registries.dto.generated.RegistryOutcome",
      "ClientDTOPage" to "it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage",
      "ClientDTO" to "it.gov.pagopa.pu.auth.dto.generated.ClientDTO",
      "CreateClientRequest" to "it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest",
      "AssessmentsRegistryExtendedDTO" to "it.gov.pagopa.pu.bff.dto.AssessmentsRegistryExtendedDTO",
      "TreasuredClassificationExtendedDTO" to "it.gov.pagopa.pu.bff.dto.TreasuredClassificationExtendedDTO",
      "Organization" to "it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO",
      "OrganizationStatus" to "it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus",
      "SpontaneousForm" to "it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm",
      "SpontaneousFormStructure" to "it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousFormStructure",
      "OrganizationAdditionalLanguage" to "it.gov.pagopa.pu.organization.dto.generated.OrganizationAdditionalLanguage",
      "OrgSubUnit" to "it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit",
      "OrgSubUnitRequestBody" to "it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody"
    )
  )
  configOptions.set(
    mapOf(
      "dateLibrary" to "java8",
      "requestMappingMode" to "api_interface",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "interfaceOnly" to "true",
      "useTags" to "true",
      "useBeanValidation" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
}

var targetEnv = when (Objects.requireNonNullElse(
  System.getProperty("targetBranch"),
  grgit.branch.current().name
)) {
  "uat" -> "uat"
  "main" -> "main"
  else -> "develop"
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateP4PAAUTH") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-auth.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.auth.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.auth.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateORGANIZATION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-organization.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.organization.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.organization.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateDEBTPOSITIONS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-debt-positions.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.debtpositions.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.debtpositions.dto.generated")
  typeMappings.set(
    mapOf(
      "LocalDateTime" to "java.time.LocalDateTime",
      "string+binary" to "Resource"
    )
  )
  importMappings.set(
    mapOf(
      "Resource" to "org.springframework.core.io.Resource"
    )
  )
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  additionalProperties.set(
    mapOf(
      "removeEnumValuePrefix" to "false"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePROCESSEXECUTIONS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-process-executions.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.processexecutions.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.processexecutions.dto.generated")
  typeMappings.set(
    mapOf(
      "LocalDateTime" to "java.time.LocalDateTime"
    )
  )
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateCLASSIFICATION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-classification.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.classification.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.classification.dto.generated")
  typeMappings.set(
    mapOf(
      "LocalDateTime" to "java.time.LocalDateTime"
    )
  )
  schemaMappings.set(
    mapOf(
      "AssessmentsRegistryRequestBody" to "it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry"
    )
  )
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGeneratePAGOPAPAYMENTS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-pagopa-payments.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.pagopapayments.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.pagopapayments.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  typeMappings.set(
    mapOf(
      "string+binary" to "Resource",
      "DebtPositionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO"
    )
  )
  importMappings.set(
    mapOf(
      "Resource" to "org.springframework.core.io.Resource"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateREGISTRIES") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-registries.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.registries.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.registries.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateWORKFLOWHUB") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-workflow-hub.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.workflowhub.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.workflowhub.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "openApiNullable" to "false",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder"
    )
  )
  library.set("resttemplate")
}
