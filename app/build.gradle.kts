plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(project(":product"))
    implementation(project(":order"))

    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
    implementation("io.micrometer:micrometer-registry-prometheus")

    runtimeOnly("com.mysql:mysql-connector-j")
}
