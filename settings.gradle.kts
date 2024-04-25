pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven{
            url =uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            credentials{
                username = "paypal_sgerritz"

            }
        }
    }
}

rootProject.name = "SVHTC_Mobile"
include(":app")
 