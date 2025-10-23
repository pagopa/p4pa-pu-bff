package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassificationFiltersDTO {
  private String iuv;
  private String iuf;
  private List<String> debtPositionTypeOrgCodes;
  private List<ClassificationsEnum> labels;
}
