package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentsFiltersDTO {

  private Long organizationId;
  private String assessmentName;
  private OffsetDateTime updateDateTimeFrom;
  private OffsetDateTime updateDateTimeTo;
  private String iuv;
  private Set<String> debtPositionTypeOrgCodes;
  private AssessmentStatus status;
}
