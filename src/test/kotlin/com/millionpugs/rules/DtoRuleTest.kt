package com.millionpugs.rules

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

@AnalyzeClasses(packages = ["com.millionpugs"])
class DtoRuleTest {
    @ArchTest
    fun `dtos must be public`(importedClasses: JavaClasses) {
        val rule = ArchRuleDefinition.classes()
            .that().resideInAPackage("dto")
            .should().haveOnlyFinalFields()
            .andShould().bePublic()
            .andShould().beTopLevelClasses()

        rule.check(importedClasses)
    }
}
