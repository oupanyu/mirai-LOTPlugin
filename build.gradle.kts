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
}
dependencies{
    api("com.alibaba:fastjson:2.0.19")
    api("org.yaml:snakeyaml:1.33")
}

