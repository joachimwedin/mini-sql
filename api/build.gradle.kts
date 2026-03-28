import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("base")
}

tasks.register("buildWasm") {
    description = "Build WASM from engine module"
    dependsOn(":engine:wasmJsBrowserProductionWebpack")
    
    doLast {
        val engineBuildDir = project(":engine").buildDir
        val engineWasmDir = File(engineBuildDir, "compileSync/wasmJs/main/productionExecutable/optimized")
        val bridgeWasmDir = File(project.projectDir, "src/generated/wasm")
        
        if (!engineWasmDir.exists()) {
            println("Warning: Engine WASM build directory not found at ${engineWasmDir.absolutePath}")
            return@doLast
        }
        
        // Ensure output directory exists
        bridgeWasmDir.mkdirs()
        
        // Copy WASM files (generated, not source code)
        val wasmFile = File(engineWasmDir, "mini-sql-engine.wasm")
        val mjsFile = File(engineWasmDir, "mini-sql-engine.mjs")
        val uninstantiatedMjsFile = File(engineWasmDir, "mini-sql-engine.uninstantiated.mjs")
        
        listOf(wasmFile, mjsFile, uninstantiatedMjsFile).forEach { srcFile ->
            if (srcFile.exists()) {
                val destFile = File(bridgeWasmDir, srcFile.name)
                Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                println("Copied ${srcFile.name} to bridge/generated/wasm/")
            } else {
                println("Warning: Expected file not found: ${srcFile.absolutePath}")
            }
        }
    }
}

tasks.register("npmInstall") {
    description = "Install npm dependencies"
    
    doLast {
        val processBuilder = ProcessBuilder("npm", "install")
        processBuilder.directory(project.projectDir)
        processBuilder.inheritIO()
        
        val process = processBuilder.start()
        val exitCode = process.waitFor()
        
        if (exitCode != 0) {
            throw GradleException("npm install failed with exit code $exitCode")
        }
    }
}

tasks.register("npmBuild") {
    description = "Compile TypeScript to JavaScript"
    dependsOn("buildWasm", "npmInstall")
    
    doLast {
        val processBuilder = ProcessBuilder("npm", "run", "build")
        processBuilder.directory(project.projectDir)
        processBuilder.inheritIO()
        
        val process = processBuilder.start()
        val exitCode = process.waitFor()
        
        if (exitCode != 0) {
            throw GradleException("TypeScript build failed with exit code $exitCode")
        }
    }
}

// Make sure WASM is built and TypeScript is compiled during assembly
tasks.named("assemble").configure {
    dependsOn("npmBuild")
}

tasks.register("dev") {
    description = "Watch and rebuild TypeScript on changes"
    dependsOn("buildWasm", "npmInstall")
    
    doLast {
        val processBuilder = ProcessBuilder("npm", "run", "dev")
        processBuilder.directory(project.projectDir)
        processBuilder.inheritIO()
        
        val process = processBuilder.start()
        process.waitFor()
    }
}

