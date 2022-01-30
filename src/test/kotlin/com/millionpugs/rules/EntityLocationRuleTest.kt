package com.millionpugs.rules

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.Table

@AnalyzeClasses(packages = ["com.millionpugs"])
class EntityLocationRuleTest {
    @ArchTest
    fun `entities should be appropriately annotated`(importedClasses: JavaClasses) {
        val rule = classes()
            .that().resideInAPackage("..entity..")
            .should().beAnnotatedWith(Entity::class.java)
            .andShould().beAnnotatedWith(Table::class.java)

        rule.check(importedClasses)
    }
}
