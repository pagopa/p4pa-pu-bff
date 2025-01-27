import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.createFile
import kotlin.io.path.notExists

plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("org.sonarqube") version "6.0.1.5171"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("org.openapi.generator") version "7.10.0"
  id("org.ajoberstar.grgit") version "5.3.0"
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

val springDocOpenApiVersion = "2.7.0"
val openApiToolsVersion = "0.2.6"
val micrometerVersion = "1.4.1"

val wiremockVersion = "3.10.0"
val wiremockSpringBootVersion = "2.1.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
  implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
  implementation("org.springframework.data:spring-data-commons")


	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	//	Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.mockito:mockito-core")
	testImplementation ("org.projectlombok:lombok")
  testImplementation ("org.wiremock:wiremock-standalone:$wiremockVersion")
  testImplementation ("com.maciejwalkowiak.spring:wiremock-spring-boot:$wiremockSpringBootVersion")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
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
    "openApiGenerateDEBTPOSITIONS"
  )
}

configure<SourceSetContainer> {
	named("main") {
		java.srcDir("$projectDir/build/generated/src/main/java")
	}
}

springBoot {
	mainClass.value("it.gov.pagopa.pu.bff.PiattaformaUnitariaBffApplication")
}

abstract class FilterTask : DefaultTask() {
  @get:Input
  abstract val srcInputPath: Property<String>
  @get:Input
  abstract val filteredFileName: Property<String>
  @get:Input
  abstract val projectDir: Property<String>
  @get:Input
  abstract val jsonOpenapi: Property<Boolean>
  @get:Input
  abstract val remoteUrl: Property<Boolean>

  @TaskAction
  fun run(){
    val inputSpecPath = if (remoteUrl.get()) {
      downloadRemoteFile(srcInputPath.get())
    } else {
      Paths.get(srcInputPath.get())
    }
    val openApiContent = Files.readString(inputSpecPath)
    val objectMapper = ObjectMapper();
    val content: Map<String, Any> = if(jsonOpenapi.get()){
      objectMapper.readValue(openApiContent, object : TypeReference<Map<String, Any>>() {})
    }else{
      Yaml().load(openApiContent)
    }

    val paths = (content["paths"] as? Map<*, *>)
      ?.mapKeys { it.key.toString() }
      ?.mapValues { (_, operations) ->
        val operationMap = operations as? Map<*, *> ?: emptyMap<String, Any>()
        operationMap.mapKeys { it.key.toString() }
          .mapValues { (_, operationDetails) ->
            val mutableOperationDetails = (operationDetails as? Map<*, *>)?.toMutableMap() ?: mutableMapOf()

            val parameters = mutableOperationDetails["parameters"] as? List<*>
            val unfiltered = parameters
              ?.filterIsInstance<Map<*, *>>()
              ?.filter { it["x-ignore"] == true }
              ?.map { it.mapKeys { param -> param.key.toString() } }
            if(!unfiltered.isNullOrEmpty() && mutableOperationDetails["x-spring-paginated"]==null){
              mutableOperationDetails["x-spring-paginated"] = true
            }
            val filteredParameters = parameters
              ?.filterIsInstance<Map<*, *>>()
              ?.filterNot { it["x-ignore"] == true }
              ?.map { it.mapKeys { param -> param.key.toString() } }

            if(!filteredParameters.isNullOrEmpty()){
              mutableOperationDetails["parameters"] = filteredParameters
            }else{
              mutableOperationDetails.remove("parameters")
            }
            mutableOperationDetails
          }
      }

    if (paths != null) {
      val mutableContent = content.toMutableMap()
      mutableContent["paths"] = paths
      val outputFile = Paths.get("${projectDir.get()}/build/${filteredFileName.get()}")
      if(!Files.exists(outputFile.parent)){
        Files.createDirectories(outputFile.parent)
      }
      if(outputFile.notExists()){
        outputFile.createFile()
      }
        Files.writeString(
          outputFile,
          Yaml(DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
          }).dump(mutableContent)
        )
      println("Filtered OpenAPI spec generated at: $outputFile")
    } else {
      throw IllegalStateException("Could not parse 'paths' from OpenAPI.")
    }
  }

  private fun downloadRemoteFile(remoteUrl: String): java.nio.file.Path {
    val fileName = remoteUrl.substringAfterLast("/")
    val tempFile = Files.createTempFile(fileName, "")
    println("Downloading remote file from $remoteUrl to ${tempFile.toAbsolutePath()}")
    URI(remoteUrl).toURL().openStream().use { inputStream ->
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING)
    }
    return tempFile
  }
}

tasks.register<FilterTask>("filterPaginatedOpenAPIBFF") {
  group = "openapi"
  description= "description"

  srcInputPath.set("${rootDir}/openapi/p4pa-pu-bff.openapi.yaml")
  filteredFileName.set("openapi-BFF-filtered.yaml")
  projectDir.set(rootDir.absolutePath)
  jsonOpenapi.set(false)
  remoteUrl.set(false)
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateBFF") {
  group = "openapi"
  description = "description"

  generatorName.set("spring")
  inputSpec.set("$rootDir/build/openapi-BFF-filtered.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.bff.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.bff.dto.generated")
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "useBeanValidation" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "additionalModelTypeAnnotations" to "@lombok.Builder"
  ))
  dependsOn("filterPaginatedOpenAPIBFF")
}

var targetEnv = when (grgit.branch.current().name) {
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
  apiPackage.set("it.gov.pagopa.pu.p4paauth.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.p4paauth.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateORGANIZATION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-organization/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.p4pa-organization.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.p4pa-organization.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateDEBTPOSITIONS") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-debt-positions/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.p4pa-debt-positions.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.p4pa-debt-positions.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true"
  ))
  library.set("resttemplate")
}
