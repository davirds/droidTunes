// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
    alias(libs.plugins.diffplug.spotless)
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.46.1").editorConfigOverride(
            mapOf(
                "ktlint_code_style" to "android",
                "ij_kotlin_allow_trailing_comma" to true,
                // These rules were introduced in ktlint 0.46.0 and should not be
                // enabled without further discussion. They are disabled for now.
                // See: https://github.com/pinterest/ktlint/releases/tag/0.46.0
                "disabled_rules" to
                    "filename," +
                    "annotation,annotation-spacing," +
                    "argument-list-wrapping," +
                    "double-colon-spacing," +
                    "enum-entry-name-case," +
                    "multiline-if-else," +
                    "no-empty-first-line-in-method-block," +
                    "package-name," +
                    "trailing-comma," +
                    "spacing-around-angle-brackets," +
                    "spacing-between-declarations-with-annotations," +
                    "spacing-between-declarations-with-comments," +
                    "unary-op-spacing"
            )
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}
