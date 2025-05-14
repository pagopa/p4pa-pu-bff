package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypesApi;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeRequestBodyMapper;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


@Slf4j
@RestController
public class DebtPositionTypeController implements DebtPositionTypesApi {

  private final DebtPositionTypeRetrieverService debtPositionTypeRetrieverService;
  private final DebtPositionTypeRequestBodyMapper debtPositionTypeRequestBodyMapper;

  public DebtPositionTypeController(DebtPositionTypeRetrieverService debtPositionTypeRetrieverService, DebtPositionTypeRequestBodyMapper debtPositionTypeRequestBodyMapper) {
    this.debtPositionTypeRetrieverService = debtPositionTypeRetrieverService;
    this.debtPositionTypeRequestBodyMapper = debtPositionTypeRequestBodyMapper;
  }

  @Override
  public ResponseEntity<DebtPositionType> getDebtPositionType(String id) {
    log.info("User requested getDebtPositionType()");
    return new ResponseEntity<>(debtPositionTypeRetrieverService.getDebtPositionTypeById(SecurityUtils.getAccessToken(), Long.valueOf(id)), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeWithCount> getDebtPositionTypeWithCount(
    Long organizationId, String description, Pageable pageable) {
    log.info("User requested getDebtPositionTypeWithCount having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeRetrieverService.getDebtPositionTypeWithCount(
      organizationId, description, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<DebtPositionTypeDetailDTO> getDebtPositionTypeDetail(
    Long organizationId, Long debtPositionTypeId) {
    log.info("User requested getDebtPositionTypeDetail having organizationId {} and ID {}", organizationId, debtPositionTypeId);
    return ResponseEntity.ofNullable(
      debtPositionTypeRetrieverService.getDebtPositionTypeDetail(organizationId,
        debtPositionTypeId, SecurityUtils.getLoggedUser(),
        SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<DebtPositionType> createDebtPositionType(
    DebtPositionTypeRequestBody body) {
    log.info("User requested createDebtPositionType()");
    return new ResponseEntity<>(debtPositionTypeRetrieverService.createDebtPositionType(
      debtPositionTypeRequestBodyMapper.map(body, Objects.requireNonNull(SecurityUtils.getLoggedUser()).getBrokerId())
      , SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<DebtPositionType> patchDebtPositionType(
    Long debtPositionTypeId,
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody) {
    log.info("User requested patchDebtPositionType having debtPositionTypeId {}", debtPositionTypeId);
    return ResponseEntity.ofNullable(debtPositionTypeRetrieverService.patchDebtPositionType(
      debtPositionTypeId, debtPositionTypePatchRequestBody, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deleteDebtPositionType(Long debtPositionTypeId) {
    log.info("User requested deleteDebtPositionType having ID {}", debtPositionTypeId);
    debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<List<DebtPositionType>> getDebtPositionTypesByOrganizationId(Long organizationId) {
    log.info("User requested getDebtPositionTypesByOrganizationId having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeRetrieverService.getDebtPositionTypesByOrganizationId(
      organizationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
