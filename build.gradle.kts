import java.util.regex.Pattern.compile




plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.13.0-RC2"
}

group = "top.oupanyu"
version = "0.2.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
    maven("https://repository.groupdocs.com/repo/")
}
dependencies{
    api("com.alibaba:fastjson:2.0.19")
    api("org.yaml:snakeyaml:1.33")
    //ChatGPT dependencies
    api("com.github.plexpt:chatgpt:1.0.3")
    api("org.springframework:spring-context:5.0.5.RELEASE")
    api("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    api("net.dreamlu:mica-http:2.7.6")
    api("net.dreamlu:mica-core:2.7.6")
}

