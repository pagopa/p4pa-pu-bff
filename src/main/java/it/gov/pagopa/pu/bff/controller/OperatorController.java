package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OperatorsApi;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sub_unit.OrgSubUnitRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.SubUnitType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Slf4j
public class OperatorController implements OperatorsApi {
  private final OperatorRetrieverService operatorRetrieverService;
  private final OrgSubUnitRetrieverService subUnitRetrieverService;

  public OperatorController(OperatorRetrieverService operatorRetrieverService, OrgSubUnitRetrieverService subUnitRetrieverService) {
    this.operatorRetrieverService = operatorRetrieverService;
    this.subUnitRetrieverService = subUnitRetrieverService;
  }

  @Override
  public ResponseEntity<PagedOrganizationOperator> getOrganizationOperators(Long organizationId, String firstName, String lastName, String fiscalCode, Pageable pageable) {
    log.info("User requested getOrganizationOperators having organizationId {}", organizationId);
    return ResponseEntity.ok(operatorRetrieverService.getOrganizationOperators(organizationId,firstName,lastName,fiscalCode,pageable,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<OperatorsDetail> getOperatorDetails(Long organizationId, String mappedExternalUserId, String debtPositionTypeOrgCode, String debtPositionTypeOrgDescription, Long debtPositionTypeId, Pageable pageable) {
    log.info("User requested getOperatorDetails having organizationId {} and mappedExternalUserId {}", organizationId, mappedExternalUserId);
    return ResponseEntity.ok(operatorRetrieverService.getOperatorDetails(new OperatorDetailsFiltersDTO(
      organizationId,
      mappedExternalUserId,
      debtPositionTypeOrgCode,
      debtPositionTypeOrgDescription,
      debtPositionTypeId
    ), pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Integer> removeDebtPositionTypeOrgFromOperator(Long organizationId, String mappedExternalUserId, Long debtPositionTypeOrgId) {
    log.info("User requested removeDebtPositionTypeOrgFromOperator having organizationId {}, debtPositionTypeOrgId {} and mappedExternalUserId {}", organizationId, debtPositionTypeOrgId, mappedExternalUserId);
    return ResponseEntity.ok(operatorRetrieverService.removeDebtPositionTypeOrgFromOperator(
      organizationId, mappedExternalUserId, debtPositionTypeOrgId, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeOrgDTO> getDebtPositionTypeOrgsNotEnabledForOperator(Long organizationId, String mappedExternalUserId, String debtPositionTypeOrgCode,
      String debtPositionTypeOrgDescription, Long debtPositionTypeId, Pageable pageable) {
    log.info("User requested getDebtPositionTypeOrgsNotEnabledForOperator having organizationId {} and mappedExternalUserId {}", organizationId, mappedExternalUserId);
    return ResponseEntity.ok(operatorRetrieverService.getDebtPositionTypeOrgsNotEnabledForOperator(new OperatorDetailsFiltersDTO(
        organizationId,
        mappedExternalUserId,
        debtPositionTypeOrgCode,
        debtPositionTypeOrgDescription,
        debtPositionTypeId
    ), pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> enableDebtPositionTypeOrgsForOperator(Long organizationId, String mappedExternalUserId, Set<Long> debtPositionTypeOrgIds) {
    log.info("User requested enableDebtPositionTypeOrgsForOperator having organizationId {}, mappedExternalUserId {} and debtPositionTypeOrgIds {}", organizationId, mappedExternalUserId, debtPositionTypeOrgIds);
    operatorRetrieverService.enableDebtPositionTypeOrgsForOperator(organizationId, mappedExternalUserId, debtPositionTypeOrgIds, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<PagedOrgSubUnit> getOperatorOrgSubUnits(Long organizationId, String mappedExternalUserId, String subUnitCode, OrgSubUnitStatus status, SubUnitType subUnitType, Pageable pageable) {
    log.info("User requested getOperatorOrgSubUnits having organizationId {}", organizationId);
    PagedOrgSubUnitFiltersDTO filters = new PagedOrgSubUnitFiltersDTO(
      organizationId,
      mappedExternalUserId,
      subUnitCode,
      status,
      subUnitType
    );
    return ResponseEntity.ok(subUnitRetrieverService.getPagedOrgSubUnits(filters, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
