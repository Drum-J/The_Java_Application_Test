package javatest.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchUnitTest {

    @Test
    void packageDependencyTest() throws Exception {
        //given
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS) // 테스트 패키지 제외
                .importPackages("javatest");

        //when
        ArchRule domainPackageRule = classes().that().resideInAPackage("..domain..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage("..study..", "..member..", "..domain..");

        //then
        domainPackageRule.check(classes);
    }

    @Test
    void packageDependencyTest2() throws Exception {
        //given
        JavaClasses classes = new ClassFileImporter().importPackages("javatest");

        //when
        ArchRule memberPackageRule = noClasses().that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..member..");

        //then
        memberPackageRule.check(classes);

    }

    @Test
    void studyPackageRule() throws Exception {
        //given
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS) // 테스트 패키지 제외
                .importPackages("javatest");

        //when
        ArchRule studyPackageRule = noClasses().that().resideOutsideOfPackages("..study..")
                .should().accessClassesThat().resideInAPackage("..study..");
        //then
        studyPackageRule.check(classes);
    }

    @Test
    void freeOfCycles() throws Exception {
        //given
        JavaClasses classes = new ClassFileImporter().importPackages("javatest");

        //when
        ArchRule freeOfCycles = slices().matching("..javatest.(*)..")
                .should().beFreeOfCycles();

        //then
        freeOfCycles.check(classes);

    }
}
