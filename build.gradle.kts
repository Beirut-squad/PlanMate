plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //Koin
    implementation("io.insert-koin:koin-core:4.0.2")
    testImplementation(kotlin("test"))
    //Test
    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    //MD5 Hashing

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}