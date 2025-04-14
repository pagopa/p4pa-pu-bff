package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgOperatorsMapper;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPosition;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class DebtPositionTypeOrgRetrieverServiceImpl implements DebtPositionTypeOrgRetrieverService {

  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService;
  private final DebtPositionService debtPositionService;
  private final AuthorizationService authorizationService;
  private final AuthzService authzService;
  private final DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper;
  private final DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper;

  public DebtPositionTypeOrgRetrieverServiceImpl(
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService,
    DebtPositionService debtPositionService,
    AuthorizationService authorizationService,
    AuthzService authzService,
    DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper,
    DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
    this.debtPositionService = debtPositionService;
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
  public void deleteDebtPositionTypeOrg(Long organizationId,
    Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    PagedModelDebtPosition debtPositions = debtPositionService.getDebtPositionByDebtPositionTypeOrgId(
      debtPositionTypeOrgId, PageRequest.of(0,1),accessToken);
    if(debtPositions!=null && debtPositions.getEmbedded()!=null && !CollectionUtils.isEmpty(debtPositions.getEmbedded().getDebtPositions())){
      throw new ConflictException("Cannot delete DebtPositionTypeOrg: There are still DebtPositions that reference it.");
    }
    debtPositionTypeOrgService.deleteDebtPositionTypeOrg(debtPositionTypeOrgId,accessToken);
  }

  @Override
  public PagedDebtPositionTypeOrgOperatorDTO getDebtPositionTypeOrgOperators(
    Long organizationId, Long debtPositionTypeOrgId, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    UserOrganizationRoles userOrganizationRole = loggedUser.getOrganizations().stream()
      .filter(o -> organizationId.equals(o.getOrganizationId()))
      .findFirst()
      .orElseThrow(IllegalArgumentException::new);
    OperatorsPage operatorsPage = getOrganizationOperators(userOrganizationRole.getOrganizationIpaCode(), accessToken);

    List<DebtPositionTypeOrgOperators> debtPositionTypeOrgOperators = new ArrayList<>();
    if (debtPositionTypeOrgId != null) {
      CollectionModelDebtPositionTypeOrgOperators collectionModelDebtPositionTypeOrgOperators =
        debtPositionTypeOrgOperatorsService.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);
      if (collectionModelDebtPositionTypeOrgOperators != null &&
        collectionModelDebtPositionTypeOrgOperators.getEmbedded() != null &&
        !CollectionUtils.isEmpty(collectionModelDebtPositionTypeOrgOperators.getEmbedded().getDebtPositionTypeOrgOperatorses())) {
        debtPositionTypeOrgOperators.addAll(collectionModelDebtPositionTypeOrgOperators.getEmbedded().getDebtPositionTypeOrgOperatorses());
      }
    }

    return debtPositionTypeOrgOperatorsMapper.mapToPagedDebtPositionTypeOrgOperatorDTO(operatorsPage, debtPositionTypeOrgOperators);
  }

  private OperatorsPage getOrganizationOperators(String organizationIpaCode, String accessToken) {
    return authzService.getOrganizationOperators(organizationIpaCode, null, null, null, 0, 10, accessToken);
  }
}
