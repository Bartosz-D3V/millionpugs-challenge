package com.millionpugs.rules

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.Test

@AnalyzeClasses(packages = ["com.millionpugs"])
class JunitRuleTest {
    @ArchTest
    fun `junit tests must return void`(importedClasses: JavaClasses) {
        val rule = ArchRuleDefinition.methods()
            .that().areAnnotatedWith(Test::class.java)
            .should().haveRawReturnType("void")

        rule.check(importedClasses)
    }
}
