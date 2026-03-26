plugins {
    kotlin("multiplatform") version "2.2.21"
}

repositories {
    mavenCentral()
}

kotlin {
    wasmJs {
        binaries.executable()
        nodejs()
    }

    sourceSets {
        wasmJsMain{
            dependencies {
                implementation(project(":core"))
            }
        }
    }
}