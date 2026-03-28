import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("base")
}

tasks.register("copyApiDist") {
    description = "Copy API dist folder into playground src/generated/lib/api"

    doLast {
        val apiDistDir = project(":api").projectDir.resolve("dist")
        val playgroundLibDir = project.projectDir.resolve("src/generated/lib/api")
        
        if (!apiDistDir.exists()) {
            println("Warning: API dist directory not found at ${apiDistDir.absolutePath}")
            return@doLast
        }
        
        // Clear and recreate the target directory
        playgroundLibDir.deleteRecursively()
        playgroundLibDir.mkdirs()
        
        // Copy API dist recursively
        apiDistDir.walkTopDown().forEach { srcFile ->
            val relativePath = srcFile.relativeToOrNull(apiDistDir) ?: return@forEach
            val destFile = playgroundLibDir.resolve(relativePath.path)
            
            if (srcFile.isDirectory) {
                destFile.mkdirs()
            } else {
                destFile.parentFile?.mkdirs()
                Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        }
        
        println("Copied API dist to playground/src/lib/api")
    }
}

tasks.named("assemble") {
    dependsOn("copyApiDist")
}

