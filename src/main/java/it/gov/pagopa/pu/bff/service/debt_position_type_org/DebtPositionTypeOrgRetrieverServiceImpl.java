package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgOperatorsMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeOrgRetrieverServiceImpl implements DebtPositionTypeOrgRetrieverService {

  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService;
  private final AuthorizationService authorizationService;
  private final AuthzService authzService;
  private final DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper;
  private final DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper;

  public DebtPositionTypeOrgRetrieverServiceImpl(
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService,
    AuthorizationService authorizationService,
    AuthzService authzService,
    DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper,
    DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
    this.authorizationService = authorizationService;
    this.authzService = authzService;
    this.debtPositionTypeOrgWithCountMapper = debtPositionTypeOrgWithCountMapper;
    this.debtPositionTypeOrgOperatorsMapper = debtPositionTypeOrgOperatorsMapper;
  }

  @Override
  public DebtPositionTypeOrg getDebtPositionTypeOrgById(Long organizationId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return debtPositionTypeOrgService.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
  }

  @Override
  public List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    CollectionModelDebtPositionTypeOrg collection = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, loggedUser.getMappedExternalUserId(), accessToken);

    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return collection.getEmbedded().getDebtPositionTypeOrgs();
  }

  @Override
  public PagedDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return debtPositionTypeOrgWithCountMapper.mapToPagedDebtPositionTypeOrgWithCount(
      debtPositionTypeOrgService.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken));
  }

  @Override
  public PagedDebtPositionTypeOrgOperatorDTO getDebtPositionTypeOrgOperators(
    Long organizationId, Long debtPositionTypeOrgId, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    OperatorsPage operatorsPage = authzService.getOrganizationOperators(
      getUserOrganizationIpaCode(organizationId, loggedUser), null, null, null,
      pageable.getPageNumber(), pageable.getPageSize(), accessToken);

    CollectionModelDebtPositionTypeOrgOperators collectionModelDebtPositionTypeOrgOperators = null;
    if (debtPositionTypeOrgId != null) {
      collectionModelDebtPositionTypeOrgOperators =
        debtPositionTypeOrgOperatorsService.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);
    }

    return debtPositionTypeOrgOperatorsMapper.mapToPagedDebtPositionTypeOrgOperatorDTO(operatorsPage, collectionModelDebtPositionTypeOrgOperators);
  }

  private String getUserOrganizationIpaCode(Long organizationId, UserInfo loggedUser) {
    return loggedUser.getOrganizations().stream()
      .filter(o -> organizationId.equals(o.getOrganizationId()))
      .findFirst()
      .orElseThrow(IllegalArgumentException::new)
      .getOrganizationIpaCode();
  }
}
