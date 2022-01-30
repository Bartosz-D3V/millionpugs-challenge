package com.millionpugs.rules

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

@AnalyzeClasses(packages = ["com.millionpugs"])
class BeansLocationRuleTest {
    @ArchTest
    fun `controller should be inside controller folder`(importedClasses: JavaClasses) {
        val rule = classes()
            .that().resideInAPackage("..controller..")
            .and().areAnnotatedWith(RestController::class.java)
            .or().areAnnotatedWith(Controller::class.java)
            .should().haveSimpleNameEndingWith("Controller")

        rule.check(importedClasses)
    }

    @ArchTest
    fun `service should be inside service folder`(importedClasses: JavaClasses) {
        val rule = classes()
            .that().resideInAPackage("..service..")
            .and().areAnnotatedWith(Service::class.java)
            .should().haveSimpleNameEndingWith("ServiceImpl")

        rule.check(importedClasses)
    }

    @ArchTest
    fun `repository should be inside repository folder`(importedClasses: JavaClasses) {
        val rule = classes()
            .that().resideInAPackage("..repository..")
            .and().implement(CrudRepository::class.java)
            .should().haveSimpleNameEndingWith("Repository")
            .orShould().haveSimpleNameEndingWith("RepositoryImpl")

        rule.check(importedClasses)
    }
}
