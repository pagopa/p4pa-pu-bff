package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DebtPositionTypeOrgOperatorsMapper {

  public PagedDebtPositionTypeOrgOperatorDTO mapToPagedDebtPositionTypeOrgOperatorDTO(
    OperatorsPage operatorsPage,
    List<DebtPositionTypeOrgOperators> debtPositionTypeOrgOperators) {
    List<DebtPositionTypeOrgOperatorDTO> content = operatorsPage.getContent()
      .stream()
      .map(o -> mapToDebtPositionTypeOrgOperatorDTO(o,
        debtPositionTypeOrgOperators.stream().anyMatch(d -> o.getOperatorId().equals(d.getOperatorExternalUserId())))
      )
      .toList();

    return PagedDebtPositionTypeOrgOperatorDTO.builder()
      .content(content)
      .size(operatorsPage.getPageSize().longValue())
      .totalPages(operatorsPage.getTotalPages().longValue())
      .totalElements(operatorsPage.getTotalElements().longValue())
      .number(operatorsPage.getPageNo().longValue())
      .build();
  }

  private DebtPositionTypeOrgOperatorDTO mapToDebtPositionTypeOrgOperatorDTO(
    OperatorDTO operator, boolean isOperatorEnabled) {
    return DebtPositionTypeOrgOperatorDTO.builder()
      .operatorId(operator.getOperatorId())
      .firstName(operator.getFirstName())
      .lastName(operator.getLastName())
      .mappedExternalUserId(operator.getMappedExternalUserId())
      .enabled(isOperatorEnabled)
      .build();
  }
}
