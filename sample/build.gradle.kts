plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    id("com.ivanalvarado.cacheinvalidationindex")
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}
