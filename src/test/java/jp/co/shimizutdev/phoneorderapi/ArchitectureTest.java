package jp.co.shimizutdev.phoneorderapi;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/** アーキテクチャ依存ルールを検証する */
@AnalyzeClasses(
    packages = "jp.co.shimizutdev.phoneorderapi",
    importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

  /** domain が外側レイヤや Spring / Servlet / JPA に依存しないことを検証する */
  @ArchTest
  @SuppressWarnings("unused")
  static final ArchRule domain_should_not_depend_on_outer_layers_or_frameworks =
      noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "..application..",
              "..presentation..",
              "..infrastructure..",
              "org.springframework..",
              "jakarta.servlet..",
              "jakarta.persistence..");

  /** application が presentation と infrastructure 詳細に依存しないことを検証する */
  @ArchTest
  @SuppressWarnings("unused")
  static final ArchRule application_should_not_depend_on_presentation_or_infrastructure_details =
      noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..presentation..", "..infrastructure..");
}
