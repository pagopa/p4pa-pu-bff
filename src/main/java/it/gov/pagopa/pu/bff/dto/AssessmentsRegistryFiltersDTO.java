package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentsRegistryFiltersDTO {
  private Long organizationId;
  private Set<String> debtPositionTypeOrgCodes;
  private String sectionCode;
  private String sectionDescription;
  private String officeCode;
  private String officeDescription;
  private String assessmentCode;
  private String assessmentDescription;
  private String operatingYear;
  private AssessmentsRegistryStatus status;
}
