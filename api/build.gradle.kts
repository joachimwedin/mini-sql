import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("base")
    alias(libs.plugins.node.gradle)
}

node {
    version.set(libs.versions.node.asProvider())
    download.set(true)
}

tasks.register<NpmTask>("npmBuild") {
    dependsOn("npmInstall")
    dependsOn("buildWasm")
    args.set(listOf("run", "build"))
}

tasks.register<Copy>("buildWasm") {
    dependsOn(":engine:buildWasm")

    from(project(":engine").layout.buildDirectory.dir("wasm"))
    into(File(project.projectDir, "src/wasm"))
}

tasks.assemble {
    dependsOn("npmBuild")
}

tasks.clean {
    delete(File(project.projectDir, ".gradle"))
    delete(File(project.projectDir, "src/wasm"))
    delete(File(project.projectDir, "dist"))
    delete(File(project.projectDir, "build"))
    delete(File(project.projectDir, "node_modules"))
}