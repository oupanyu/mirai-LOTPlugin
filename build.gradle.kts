plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "top.oupanyu"
version = "0.5.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
    //maven("https://repository.groupdocs.com/repo/")
}
dependencies{
    api("org.yaml:snakeyaml:2.0")
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.github.briqt:xunfei-spark4j:1.0.0")
    //implementation()
    //ChatGPT dependencies

}

