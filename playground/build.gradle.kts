import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("base")
    alias(libs.plugins.node.gradle)
}

node {
    version.set(libs.versions.node.asProvider())
    download.set(true)
}

tasks.npmInstall {
    dependsOn(":api:assemble")
}

tasks.register<NpmTask>("npmBuild") {
    dependsOn("npmInstall")
    args.set(listOf("run", "build"))
}

tasks.register<NpmTask>("dev") {
    dependsOn("npmBuild")
    args.set(listOf("run", "dev"))
}

tasks.assemble {
    dependsOn("npmBuild")
}

tasks.clean {
    delete(File(project.projectDir, "build"))
    delete(File(project.projectDir, "dist"))
    delete(File(project.projectDir, "node_modules"))
}
