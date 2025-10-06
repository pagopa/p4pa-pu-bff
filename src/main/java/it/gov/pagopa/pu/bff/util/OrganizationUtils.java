package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.organization.dto.generated.Organization;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OrganizationUtils {
  private OrganizationUtils() {
  }

  public static Set<String> getExcludedLabels(Organization organization) {
    return getExcludedEnums(organization).stream()
      .map(ClassificationsEnum::getValue)
      .collect(Collectors.toSet());
  }

  public static Set<ClassificationsEnum> getExcludedLabelsAsEnum(Organization organization) {
    return getExcludedEnums(organization);
  }

  private static Set<ClassificationsEnum> getExcludedEnums(Organization organization) {
    Set<ClassificationsEnum> excludedEnums = new HashSet<>();

    if (Boolean.FALSE.equals(organization.getFlagPaymentNotification())) {
      excludedEnums.add(ClassificationsEnum.RT_NO_IUD);
      excludedEnums.add(ClassificationsEnum.IUD_NO_RT);
    }

    if (Boolean.FALSE.equals(organization.getFlagTreasury())) {
      excludedEnums.add(ClassificationsEnum.RT_TES);
      excludedEnums.add(ClassificationsEnum.RT_IUF_TES);
      excludedEnums.add(ClassificationsEnum.IUF_NO_TES);
      excludedEnums.add(ClassificationsEnum.TES_NO_IUF_OR_IUV);
      excludedEnums.add(ClassificationsEnum.IUF_TES_DIV_IMP);
      excludedEnums.add(ClassificationsEnum.TES_NO_MATCH);
    }

    return excludedEnums;
  }
}
