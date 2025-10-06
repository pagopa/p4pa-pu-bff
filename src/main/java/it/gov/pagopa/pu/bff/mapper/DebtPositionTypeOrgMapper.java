package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsSelection;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeOrgMapper {

  private final AuthzService authzService;
  private final Integer pageMaxSize;

  public DebtPositionTypeOrgMapper(AuthzService authzService,@Value("${rest.page.request-max-page-size}") Integer pageMaxSize) {
    this.authzService = authzService;
    this.pageMaxSize = pageMaxSize;
  }

  public SaveDebtPositionTypeOrgDTO mapToSaveDebtPositionTypeOrgDTO(
    it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO, String operatorExternalUserId, String organizationIpaCode, String accessToken) {
    if (saveDebtPositionTypeOrgDTO == null) {
      return null;
    }

    DebtPositionTypeOrg debtPositionTypeOrg = saveDebtPositionTypeOrgDTO.getDebtPositionTypeOrg();
    debtPositionTypeOrg.setFlagActive(true);

    return SaveDebtPositionTypeOrgDTO.builder()
      .debtPositionTypeOrg(debtPositionTypeOrg)
      .enabledOperators(getEnabledOperators(saveDebtPositionTypeOrgDTO.getEnabledOperators(),operatorExternalUserId,saveDebtPositionTypeOrgDTO.getOperatorsSelection(), organizationIpaCode, accessToken))
      .disabledOperators(OperatorsSelection.SELECTED.equals(saveDebtPositionTypeOrgDTO.getOperatorsSelection())?saveDebtPositionTypeOrgDTO.getDisabledOperators():null)
      .removeEnabledOperators(OperatorsSelection.NONE.equals(saveDebtPositionTypeOrgDTO.getOperatorsSelection()))
      .build();
  }

  private Set<String> getEnabledOperators(Set<String> enabledOperators, String operatorExternalUserId, OperatorsSelection operatorsSelection, String organizationIpaCode, String accessToken) {
    Set<String> operators;
    operators = switch (operatorsSelection){
      case ALL -> retrieveOperatorsSet(organizationIpaCode,accessToken);
      case SELECTED -> enabledOperators;
      case NONE -> new HashSet<>();
    };
    operators.add(operatorExternalUserId);
    return operators;
  }

  private Set<String> retrieveOperatorsSet(String organizationIpaCode, String accessToken) {
    OperatorsPage operatorsPage = authzService.getOrganizationOperators(
      organizationIpaCode, null, null, null,
      0, pageMaxSize, accessToken);
    return operatorsPage.getContent().stream().map(OperatorDTO::getMappedExternalUserId).collect(
      Collectors.toCollection(HashSet::new));
  }


}
