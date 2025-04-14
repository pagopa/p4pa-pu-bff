package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DebtPositionTypeOrgOperatorsMapper {

  public PagedDebtPositionTypeOrgOperatorDTO mapToPagedDebtPositionTypeOrgOperatorDTO(
    OperatorsPage operatorsPage,
    CollectionModelDebtPositionTypeOrgOperators collectionModelDebtPositionTypeOrgOperators) {
    List<DebtPositionTypeOrgOperators> debtPositionTypeOrgOperators = new ArrayList<>();
    if (collectionModelDebtPositionTypeOrgOperators != null &&
      collectionModelDebtPositionTypeOrgOperators.getEmbedded() != null &&
      !CollectionUtils.isEmpty(collectionModelDebtPositionTypeOrgOperators.getEmbedded().getDebtPositionTypeOrgOperatorses())) {
      debtPositionTypeOrgOperators.addAll(collectionModelDebtPositionTypeOrgOperators.getEmbedded().getDebtPositionTypeOrgOperatorses());
    }

    HashSet<String> debtPositionTypeOrgOperatorsIds = debtPositionTypeOrgOperators.stream()
      .map(DebtPositionTypeOrgOperators::getOperatorExternalUserId)
      .collect(Collectors.toCollection(HashSet::new));

    List<DebtPositionTypeOrgOperatorDTO> content = operatorsPage.getContent()
      .stream()
      .map(o -> mapToDebtPositionTypeOrgOperatorDTO(o, debtPositionTypeOrgOperatorsIds))
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
    OperatorDTO operator, HashSet<String> debtPositionTypeOrgOperatorIds) {
    return DebtPositionTypeOrgOperatorDTO.builder()
      .operatorId(operator.getOperatorId())
      .firstName(operator.getFirstName())
      .lastName(operator.getLastName())
      .mappedExternalUserId(operator.getMappedExternalUserId())
      .enabled(debtPositionTypeOrgOperatorIds.contains(operator.getOperatorId()))
      .build();
  }
}
