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
        val bridgeWasmDir = File(project.projectDir, "src/wasm")
        
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
