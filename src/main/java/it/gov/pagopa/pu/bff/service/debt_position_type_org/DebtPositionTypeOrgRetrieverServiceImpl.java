package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionTypeOrgException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgOperatorsMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
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
  private final AuthorizationService authorizationService;
  private final DebtPositionService debtPositionService;
  private final AuthzService authzService;
  private final DebtPositionTypeService debtPositionTypeService;
  private final DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper;
  private final DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper;
  private final DebtPositionTypeOrgMapper debtPositionTypeOrgMapper;

  public DebtPositionTypeOrgRetrieverServiceImpl(
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService,
    DebtPositionService debtPositionService,
    AuthorizationService authorizationService,
    AuthzService authzService, DebtPositionTypeService debtPositionTypeService,
    DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper,
    DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper,
    DebtPositionTypeOrgMapper debtPositionTypeOrgMapper) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
    this.debtPositionService = debtPositionService;
    this.authorizationService = authorizationService;
    this.authzService = authzService;
    this.debtPositionTypeService = debtPositionTypeService;
    this.debtPositionTypeOrgWithCountMapper = debtPositionTypeOrgWithCountMapper;
    this.debtPositionTypeOrgOperatorsMapper = debtPositionTypeOrgOperatorsMapper;
    this.debtPositionTypeOrgMapper = debtPositionTypeOrgMapper;
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

  @Override
  public DebtPositionTypeOrg createDebtPositionTypeOrg(Long organizationId,
    SaveDebtPositionTypeOrgDTO createDebtPositionTypeOrgDTO, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    validateDebtPositionTypeOrg(organizationId, createDebtPositionTypeOrgDTO.getDebtPositionTypeOrg(), loggedUser.getBrokerId(),
      accessToken);

    return debtPositionTypeOrgService.saveDebtPositionTypeOrg(
      debtPositionTypeOrgMapper.mapToSaveDebtPositionTypeOrgDTO(createDebtPositionTypeOrgDTO,
        loggedUser.getMappedExternalUserId(),  getUserOrganizationIpaCode(organizationId, loggedUser),accessToken),
      accessToken
    );
  }

  private void validateDebtPositionTypeOrg(Long organizationId,
    DebtPositionTypeOrg debtPositionTypeOrgDTO,
    Long brokerId, String accessToken) {
    if(!organizationId.equals(
      debtPositionTypeOrgDTO.getOrganizationId())){
      throw new InvalidDebtPositionTypeOrgException("The DebtPositionTypeOrg's organizationId "+ debtPositionTypeOrgDTO.getOrganizationId()+
        " does not match the given organizationId "+ organizationId);
    }
    if(debtPositionTypeOrgDTO.getDebtPositionTypeOrgId()!=null){
      throw new InvalidDebtPositionTypeOrgException("DebtPositionTypeOrgId should not be provided");
    }
    DebtPositionType debtPositionType = debtPositionTypeService.getDebtPositionTypeById(
      debtPositionTypeOrgDTO.getDebtPositionTypeId(),
      accessToken);
    if(debtPositionType==null){
      throw new InvalidDebtPositionTypeOrgException("DebtPositionType having id "+debtPositionTypeOrgDTO.getDebtPositionTypeId()+" not found");
    }
    if(!debtPositionType.getBrokerId().equals(brokerId)){
      throw new InvalidDebtPositionTypeOrgException("The brokerId "+brokerId+" does not match the given DebtPositionType's brokerId");
    }
  }
}
