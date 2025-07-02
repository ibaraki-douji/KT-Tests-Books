package fr.ibaraki.books

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import io.kotest.core.spec.style.FunSpec

class ArchTest : FunSpec() {

    val basePackage = "fr.ibaraki.books"

    init {

        test("it should respect the clean architecture concept") {

            val importedClasses: JavaClasses = ClassFileImporter()
                .withImportOption(ImportOption.DoNotIncludeTests())
                .importPackages(basePackage)

            val rule = layeredArchitecture().consideringAllDependencies()
                .layer("model").definedBy("$basePackage.domain.model..")
                .layer("usecase").definedBy("$basePackage.domain.usecase..")
                .layer("port").definedBy("$basePackage.domain.port..")
                .layer("infrastructure").definedBy("$basePackage.infrastructure..")
                .layer("Standard API").definedBy("java..", "kotlin..", "kotlinx..", "org.jetbrains.annotations..", "org.springframework..")
                .withOptionalLayers(true)
                .whereLayer("model").mayOnlyAccessLayers("Standard API")
                .whereLayer("port").mayOnlyAccessLayers("Standard API", "model")
                .whereLayer("usecase").mayOnlyAccessLayers("Standard API", "model", "port")

            rule.check(importedClasses)
        }
    }

}