package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationUtilsTest {
  @Test
  void givenOrganizationWithBothFlagsTrueWhenGetExcludedLabelsThenReturnEmptyExcludedLabels() {
    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(true);

    Set<String> excludedLabels = OrganizationUtils.getExcludedLabels(organization);

    assertNotNull(excludedLabels);
    assertTrue(excludedLabels.isEmpty());
  }

  @Test
  void givenOrganizationWithBothFlagsTrueWhenGetExcludedLabelsAsEnumThenReturnEmptyExcludedLabels() {
    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(true);

    Set<ClassificationsEnum> excludedLabels = OrganizationUtils.getExcludedLabelsAsEnum(organization);

    assertNotNull(excludedLabels);
    assertTrue(excludedLabels.isEmpty());
  }

  @Test
  void givenOrganizationWithFalsePaymentFlagWhenGetExcludedLabelsThenReturnRightExcludedLabels() {
    Organization organization = new Organization();
    organization.setFlagPaymentNotification(false);
    organization.setFlagTreasury(true);

    Set<String> expectedEnums = Set.of(
      ClassificationsEnum.RT_NO_IUD.getValue(),
      ClassificationsEnum.IUD_NO_RT.getValue()
    );

    Set<String> excludedLabels = OrganizationUtils.getExcludedLabels(organization);

    assertEquals(excludedLabels, expectedEnums);
  }

  @Test
  void givenOrganizationWithFalseTreasuryFlagWhenGetExcludedLabelsAsEnumThenReturnRightExcludedLabels() {
    Organization organization = new Organization();
    organization.setFlagPaymentNotification(false);
    organization.setFlagTreasury(true);

    Set<ClassificationsEnum> expectedEnums = Set.of(
      ClassificationsEnum.RT_NO_IUD,
      ClassificationsEnum.IUD_NO_RT
    );

    Set<ClassificationsEnum> excludedLabels = OrganizationUtils.getExcludedLabelsAsEnum(organization);

    assertEquals(excludedLabels, expectedEnums);
  }

  @Test
  void givenOrganizationWithFalseTreasuryFlagWhenGetExcludedLabelsThenReturnRightExcludedLabels() {
    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(false);

    Set<String> expectedEnums = Set.of(
      ClassificationsEnum.RT_TES.getValue(),
      ClassificationsEnum.RT_IUF_TES.getValue(),
      ClassificationsEnum.IUF_NO_TES.getValue(),
      ClassificationsEnum.TES_NO_IUF_OR_IUV.getValue(),
      ClassificationsEnum.IUF_TES_DIV_IMP.getValue(),
      ClassificationsEnum.TES_NO_MATCH.getValue()
    );

    Set<String> excludedLabels = OrganizationUtils.getExcludedLabels(organization);

    assertEquals(excludedLabels, expectedEnums);
  }

  @Test
  void givenOrganizationWithFalsePaymentFlagWhenGetExcludedLabelsAsEnumThenReturnRightExcludedLabels() {
    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(false);

    Set<ClassificationsEnum> expectedEnums = Set.of(
      ClassificationsEnum.RT_TES,
      ClassificationsEnum.RT_IUF_TES,
      ClassificationsEnum.IUF_NO_TES,
      ClassificationsEnum.TES_NO_IUF_OR_IUV,
      ClassificationsEnum.IUF_TES_DIV_IMP,
      ClassificationsEnum.TES_NO_MATCH
    );

    Set<ClassificationsEnum> excludedLabels = OrganizationUtils.getExcludedLabelsAsEnum(organization);

    assertEquals(excludedLabels, expectedEnums);
  }
}
