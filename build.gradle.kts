plugins {
    id("base")
}

tasks.assemble {
    dependsOn(":engine:assemble")
    dependsOn(":api:assemble")
    dependsOn(":playground:assemble")
}

tasks.clean {
    dependsOn(":engine:clean")
    dependsOn(":api:clean")
    dependsOn(":playground:clean")

    delete(File(project.projectDir, ".gradle"))
    delete(File(project.projectDir, "build"))
    delete(File(project.projectDir, ".kotlin"))
}