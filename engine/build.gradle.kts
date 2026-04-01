@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    wasmJs {
        binaries.executable()
        browser()
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

val buildWasm by tasks.registering(Copy::class) {
    description = "Copies the production WASM build output into build/wasm"
    dependsOn("wasmJsBrowserProductionWebpack")

    from("build/compileSync/wasmJs/main/productionExecutable/optimized")
    into(layout.buildDirectory.dir("wasm"))
}

tasks.assemble {
    dependsOn(buildWasm)
}

val generateLexerTransitions by tasks.registering(JavaExec::class) {
    description = "Runs the lexer table generator and updates LexerTransitions.kt"

    val jvmCompilation = kotlin.jvm().compilations["main"]
    mainClass.set("com.minisql.engine.lexer.gen.GeneratorKt")
    classpath = jvmCompilation.runtimeDependencyFiles + jvmCompilation.output.allOutputs

    val outputFile = project.file("src/commonMain/kotlin/com/minisql/engine/lexer/LexerTransitions.kt")
    val capture = ByteArrayOutputStream()
    standardOutput = capture

    doLast {
        val generated = capture.toString(Charsets.UTF_8)
        outputFile.writeText(generated)
    }
}
