package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeOrgBalanceCostMapper {
  public DebtPositionTypeOrgBalanceCostDTO map(DebtPositionTypeOrgBalanceCost dptobc) {
    DebtPositionTypeOrgBalanceCostDTO dto = new DebtPositionTypeOrgBalanceCostDTO();

    dto.setType(dptobc.getType());
    dto.setOperatingYear(dptobc.getOperatingYear());
    dto.setOfficeCode(dptobc.getOfficeCode());
    dto.sectionDescription(dptobc.getSectionDescription());
    dto.setSectionCode(dptobc.getSectionCode());
    dto.setSectionDescription(dptobc.getSectionDescription());
    dto.setAssessmentCode(dptobc.getAssessmentCode());
    dto.setAssessmentDescription(dptobc.getAssessmentDescription());

    return dto;
  }
}
