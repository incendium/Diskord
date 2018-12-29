group = "com.jessecorbett"
version = "0.3.1-SNAPSHOT"

val kotlinVersion: String by project
val kotlinCoroutinesVersion: String by project
val okhttpVersion: String by project
val jacksonVersion: String by project
val slf4jVersion: String by project
val junitVersion: String by project

plugins {
    `maven-publish`

    kotlin("jvm") version "1.3.11"
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)

    classifier = "sources"
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", sourcesJar)
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // Backwards Compatibility
    testImplementation("junit:junit:4.12")
    testRuntime("org.junit.vintage:junit-vintage-engine:$junitVersion")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
