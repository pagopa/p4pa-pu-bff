package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OperatorsApi;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OperatorController implements OperatorsApi {

  private final OperatorRetrieverService operatorRetrieverService;

    public OperatorController(OperatorRetrieverService operatorRetrieverService) {
        this.operatorRetrieverService = operatorRetrieverService;
    }

  @Override
  public ResponseEntity<PagedOrganizationOperator> getOrganizationOperators(Long organizationId, String firstName, String lastName, String fiscalCode, Pageable pageable) {
    log.info("User requested getOrganizationOperators having organizationId {}", organizationId);
    return ResponseEntity.ok(operatorRetrieverService.getOrganizationOperators(organizationId,firstName,lastName,fiscalCode,pageable,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<OperatorsDetail> getOperatorDetails(Long organizationId, String mappedExternalUserId, String debtPositionTypeOrgCode, String debtPositionTypeOrgDescription, Long debtPositionTypeId, Pageable pageable) {
    log.info("User requested getOperatorDetails having organizationId {} and mappedExternalUserId {}", organizationId, mappedExternalUserId);
    return ResponseEntity.ok(operatorRetrieverService.findPagedDebtPositionTypeOrg(new OperatorDetailsFiltersDTO(
      organizationId,
      mappedExternalUserId,
      debtPositionTypeOrgCode,
      debtPositionTypeOrgDescription,
      debtPositionTypeId
    ), pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
