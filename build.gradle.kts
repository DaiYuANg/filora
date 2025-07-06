plugins {
  java
  alias(libs.plugins.quarkus)
  alias(libs.plugins.lombok)
  alias(libs.plugins.dotenv)
  alias(libs.plugins.version.check)
  alias(libs.plugins.git)
}

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation(enforcedPlatform(libs.quarkus.bom))
  annotationProcessor(enforcedPlatform(libs.quarkus.bom))
  implementation(libs.quarkus.rest)
  implementation(libs.quarkus.flyway)
  implementation(libs.quarkus.jdbc.mysql)
  implementation(libs.quarkus.scheduler)
  implementation(libs.quarkus.smallrye.jwt)
  implementation(libs.quarkus.jdbc.postgresql)
  implementation(libs.quarkus.rest.jackson)
  implementation(libs.quarkus.hibernate.reactive.panache)
  implementation(libs.quarkus.reactive.mysql.client)
  implementation(libs.quarkus.agroal)
  implementation(libs.quarkus.hibernate.reactive)
  implementation(libs.quarkus.info)
  implementation(libs.quarkus.rest.client)
  implementation(libs.quarkus.config.yaml)
  implementation(libs.quarkus.jfr)
  implementation(libs.quarkus.logging.json)
  implementation(libs.quarkus.smallrye.jwt.build)
  implementation(libs.quarkus.rest.client.jackson)
  implementation(libs.quarkus.hibernate.validator)
  implementation(libs.quarkus.quartz)
  implementation(libs.quarkus.smallrye.metrics)
  implementation(libs.quarkus.arc)
  implementation(libs.quarkus.reactive.pg.client)
  implementation(libs.jetbrains.annotation)
  implementation(libs.quarkus.quinoa)
  implementation(libs.quarkus.minio)
  implementation(libs.quarkus.minio.native)
  implementation(libs.quarkus.smallrye.context.propagation)
  implementation(libs.agrona)
  implementation(libs.tika)
  annotationProcessor(libs.hibernate.jpamodelgen)
  implementation(libs.record.builder.core)
  annotationProcessor(libs.record.builder.processor)
  implementation(libs.mapstruct)
  annotationProcessor(libs.mapstruct.annotation.processor)
  implementation(libs.vavr)
  implementation(libs.quarkus.container.image.podman)
  implementation(libs.quarkus.systemd.notify)
  implementation(libs.apache.common.lang3)
  implementation(libs.quarkus.smallrye.openapi)
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.rest-assured:rest-assured")
}

group = "org.filora"
version = "1.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
}
