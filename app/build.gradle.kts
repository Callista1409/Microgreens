import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    jacoco
}

android {
    namespace = "edu.uph.m23si1.microgreens"
    compileSdk = 35

    defaultConfig {
        applicationId = "edu.uph.m23si1.microgreens"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.withType<Test>().configureEach {
    extensions.configure(JacocoTaskExtension::class.java) {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

afterEvaluate {
    val testTaskName = "testDebugUnitTest"
    val testTask = tasks.named(testTaskName, Test::class.java)

    tasks.register<JacocoReport>("jacocoTestReport") {
        group = "Reporting"
        description = "HTML/XML JaCoCo report for unit tests (termasuk Robolectric)."
        dependsOn(testTask)

        val javaClassesDir =
            layout.buildDirectory.dir("intermediates/javac/debug/compileDebugJavaWithJavac/classes")
        val excludes = listOf(
            "**/R.class",
            "**/R\$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*_Binding.class",
            "**/databinding/**"
        )

        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }

        classDirectories.setFrom(
            fileTree(javaClassesDir) { exclude(excludes) }
        )
        sourceDirectories.setFrom(files("$projectDir/src/main/java"))
        additionalSourceDirs.setFrom(files("$projectDir/src/main/java"))

        executionData.setFrom(
            fileTree(layout.buildDirectory.get().asFile) {
                setIncludes(
                    listOf(
                        "jacoco/$testTaskName.exec",
                        "outputs/unit_test_code_coverage/debugUnitTest/$testTaskName.exec"
                    )
                )
            }
        )
    }
}
