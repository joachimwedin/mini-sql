plugins {
    id("base")
}

tasks.register<Exec>("buildApiNpm") {
    description = "Build API npm module"
    workingDir = project(":api").projectDir
    commandLine("npm", "run", "build")
}

tasks.register<Exec>("npmInstall") {
    description = "Install npm dependencies for playground"
    dependsOn("buildApiNpm")
    workingDir = project.projectDir
    commandLine("npm", "install")
}

tasks.named("assemble") {
    dependsOn("npmInstall")
}

