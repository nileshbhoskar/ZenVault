plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.12"
}

// 1. ONLY include actual code-bearing modules. Remove container projects like ":feature" and ":core"
val coveragedProjects = listOf(
    project(":app"),
    project(":core:data"),
    project(":core:domain"),
    project(":core:network"),
    project(":feature:dashboard"),
    project(":feature:transaction")
)

// 2. Ensure all listed subprojects are evaluated completely before aggregating
coveragedProjects.forEach { evaluationDependsOn(it.path) }

tasks.register<JacocoReport>("jacocoRootReport") {
    group = "Reporting"
    description = "Generates an aggregate JaCoCo coverage report for all ZenVault Android modules."

    // Force unit tests across modules to run before generating the combined report
    val testTasks = coveragedProjects.mapNotNull { it.tasks.findByName("testDebugUnitTest") }
    dependsOn(testTasks)

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/androidx/**/*.*",
        "**/*MembersInjector*.*",
        "**/*_Factory*.*",
        "**/*_Provides*.*",
        "**/*_ViewBinding*.*",
        "**/Dagger*.*",
        "**/*Composable*.*" // Optional: filter out Compose synthetic classes if desired
    )

    // 3. Robust Class gathering: Scan alternative paths used by modern AGP versions
    val classDirectoriesList = coveragedProjects.map { proj ->
        val buildDir = proj.layout.buildDirectory.get().asFile

        val javaTree = fileTree("$buildDir/intermediates/javac/debug/classes") { exclude(fileFilter) }

        // Modern AGP often dumps Kotlin compilation classes under /intermediates/compile_library_classes_jar/debug
        val kotlinTree = fileTree("$buildDir/tmp/kotlin-classes/debug") { exclude(fileFilter) }
        val libraryKotlinTree = fileTree("$buildDir/intermediates/compile_library_classes_jar/debug") { exclude(fileFilter) }

        javaTree + kotlinTree + libraryKotlinTree
    }
    classDirectories.setFrom(files(classDirectoriesList))

    // 4. Map Source directories
    val sourceDirectoriesList = coveragedProjects.map { proj ->
        files(
            "${proj.projectDir}/src/main/java",
            "${proj.projectDir}/src/main/kotlin"
        )
    }
    sourceDirectories.setFrom(files(sourceDirectoriesList))

    // 5. Gather execution data safely, capturing alternative execution trees
    val executionDataList = coveragedProjects.map { proj ->
        fileTree(proj.layout.buildDirectory.get()) {
            include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
            include("jacoco/testDebugUnitTest.exec")
            include("outputs/jacoco/testDebugUnitTest.exec") // Alternate AGP fallback path
        }
    }
    executionData.setFrom(files(executionDataList))
}