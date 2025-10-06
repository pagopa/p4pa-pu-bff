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
  String iuv;
  List<String> debtPositionTypeOrgCodes;
  List<ClassificationsEnum> labels;
}
