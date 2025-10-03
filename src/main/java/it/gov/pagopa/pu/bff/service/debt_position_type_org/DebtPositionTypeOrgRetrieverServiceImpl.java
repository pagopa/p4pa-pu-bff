package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.*;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionTypeOrgException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgDTOMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgOperatorsMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.gov.pagopa.pu.bff.util.Utilities.checkImmutableField;

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
  private final DebtPositionTypeOrgDTOMapper debtPositionTypeOrgDTOMapper;
  private final OrgSilServiceRetrieverService orgSilServiceRetrieverService;
  private final SpontaneousFormRetrieverService spontaneousFormRetrieverService;

  public DebtPositionTypeOrgRetrieverServiceImpl(
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService,
    DebtPositionService debtPositionService,
    AuthorizationService authorizationService,
    AuthzService authzService, DebtPositionTypeService debtPositionTypeService,
    DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapper,
    DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapper,
    DebtPositionTypeOrgMapper debtPositionTypeOrgMapper,
    DebtPositionTypeOrgDTOMapper debtPositionTypeOrgDTOMapper,
    OrgSilServiceRetrieverService orgSilServiceRetrieverService,
    SpontaneousFormRetrieverService spontaneousFormRetrieverService) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
    this.debtPositionService = debtPositionService;
    this.authorizationService = authorizationService;
    this.authzService = authzService;
    this.debtPositionTypeService = debtPositionTypeService;
    this.debtPositionTypeOrgWithCountMapper = debtPositionTypeOrgWithCountMapper;
    this.debtPositionTypeOrgOperatorsMapper = debtPositionTypeOrgOperatorsMapper;
    this.debtPositionTypeOrgMapper = debtPositionTypeOrgMapper;
    this.debtPositionTypeOrgDTOMapper = debtPositionTypeOrgDTOMapper;
    this.orgSilServiceRetrieverService = orgSilServiceRetrieverService;
    this.spontaneousFormRetrieverService = spontaneousFormRetrieverService;
  }

  @Override
  public DebtPositionTypeOrgDTO getDebtPositionTypeOrgById(Long organizationId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
    if (debtPositionTypeOrg == null) {
      throw new ResourceNotFoundException("DebtPositionTypeOrg not found for ID: " + debtPositionTypeOrgId);
    }

    DebtPositionType debtPositionType = debtPositionTypeService.getDebtPositionTypeById(debtPositionTypeOrg.getDebtPositionTypeId(), accessToken);

    String notifyOutcomePushOrgSilServiceApplicationName = orgSilServiceRetrieverService.getOrgSilServiceApplicationName(debtPositionTypeOrg.getNotifyOutcomePushOrgSilServiceId(), accessToken);

    String amountActualizationOrgSilServiceApplicationName = orgSilServiceRetrieverService.getOrgSilServiceApplicationName(debtPositionTypeOrg.getAmountActualizationOrgSilServiceId(), accessToken);

    String spontaneousFormCode = null;
    if (debtPositionTypeOrg.getSpontaneousFormId() != null){
      SpontaneousForm spontaneousForm = spontaneousFormRetrieverService.getSpontaneousFormAndValidate(debtPositionTypeOrg.getSpontaneousFormId(), debtPositionTypeOrg, accessToken);
      spontaneousFormCode = spontaneousForm.getCode();
    }

    return debtPositionTypeOrgDTOMapper.map(debtPositionTypeOrg, debtPositionType, notifyOutcomePushOrgSilServiceApplicationName, amountActualizationOrgSilServiceApplicationName, spontaneousFormCode);
  }



  @Override
  public List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, Boolean flagActive, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return getDebtPositionTypeOrgs(organizationId, flagActive, loggedUser.getMappedExternalUserId(), accessToken);
  }

  @Override
  public List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, Boolean flagActive, String mappedExternalUserId, String accessToken) {
    CollectionModelDebtPositionTypeOrg collection = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, flagActive, mappedExternalUserId, accessToken);

    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return collection.getEmbedded().getDebtPositionTypeOrgs();
  }

  @Override
  public PagedDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Boolean flagActive, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return debtPositionTypeOrgWithCountMapper.mapToPagedDebtPositionTypeOrgWithCount(
      debtPositionTypeOrgService.getDebtPositionTypeOrgWithCount(organizationId, code, description, flagActive, pageable, accessToken));
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

  @Override
  public DebtPositionTypeOrg updateDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgId, SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    DebtPositionTypeOrg existingDebtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
    if (existingDebtPositionTypeOrg == null) {
      throw new ResourceNotFoundException("DebtPositionTypeOrg having ID %d not found".formatted(debtPositionTypeOrgId));
    }
    verifyDebtPositionTypeOrg(saveDebtPositionTypeOrgDTO.getDebtPositionTypeOrg(),existingDebtPositionTypeOrg);
    return debtPositionTypeOrgService.saveDebtPositionTypeOrg(
            debtPositionTypeOrgMapper.mapToSaveDebtPositionTypeOrgDTO(saveDebtPositionTypeOrgDTO,
                    loggedUser.getMappedExternalUserId(), getUserOrganizationIpaCode(organizationId, loggedUser),accessToken),
            accessToken
    );
  }

  private void verifyDebtPositionTypeOrg(DebtPositionTypeOrg updatedDebtPositionTypeOrg, DebtPositionTypeOrg existingDebtPositionTypeOrg) {
    List<String> modifiedFields = new ArrayList<>();
    checkImmutableField("debtPositionTypeOrgId", existingDebtPositionTypeOrg.getDebtPositionTypeOrgId(), updatedDebtPositionTypeOrg.getDebtPositionTypeOrgId(), modifiedFields);
    checkImmutableField("debtPositionTypeId", existingDebtPositionTypeOrg.getDebtPositionTypeId(), updatedDebtPositionTypeOrg.getDebtPositionTypeId(), modifiedFields);
    checkImmutableField("organizationId", existingDebtPositionTypeOrg.getOrganizationId(), updatedDebtPositionTypeOrg.getOrganizationId(), modifiedFields);
    checkImmutableField("code", existingDebtPositionTypeOrg.getCode(), updatedDebtPositionTypeOrg.getCode(), modifiedFields);
    checkImmutableField("description", existingDebtPositionTypeOrg.getDescription(), updatedDebtPositionTypeOrg.getDescription(), modifiedFields);
    checkImmutableField("orgSector", existingDebtPositionTypeOrg.getOrgSector(), updatedDebtPositionTypeOrg.getOrgSector(), modifiedFields);
    checkImmutableField("flagActive", existingDebtPositionTypeOrg.getFlagActive(), updatedDebtPositionTypeOrg.getFlagActive(), modifiedFields);
    checkImmutableField("flagAmountActualization", existingDebtPositionTypeOrg.getFlagAmountActualization(), updatedDebtPositionTypeOrg.getFlagAmountActualization(), modifiedFields);
    checkImmutableField("flagExternal", existingDebtPositionTypeOrg.getFlagExternal(), updatedDebtPositionTypeOrg.getFlagExternal(), modifiedFields);
    if(!CollectionUtils.isEmpty(modifiedFields)){
      throw new ValidationException("The following DebtPositionTypeOrg fields are readOnly. "+modifiedFields);
    }
  }

  @Override
  public void validateOperator(Long organizationId, String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken) {
    getDebtPositionTypeOrgByCode(organizationId, debtPositionTypeOrgCode, mappedExternalUserId, accessToken);
  }

  @Override
  public Set<String> getDebtPositionTypeOrgCodes(Long organizationId, Boolean flagActive, String mappedExternalUserId, String accessToken) {
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = getDebtPositionTypeOrgs(organizationId,flagActive, mappedExternalUserId, accessToken);
    if (!CollectionUtils.isEmpty(debtPositionTypeOrgs)) {
      return debtPositionTypeOrgs.stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());
    } else {
      return Collections.emptySet();
    }
  }

  @Override
  public void validateIuds(Long organizationId, String debtPositionTypeOrgCode, Set<String> iuds, String accessToken) {
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = debtPositionTypeOrgService.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId, iuds, accessToken);
    if(debtPositionTypeOrgs.size()!=1 || !debtPositionTypeOrgs.getFirst().getCode().equals(debtPositionTypeOrgCode)){
      throw new IllegalArgumentException("One or more iuds refer to an invalid DebtPositionTypeOrg");
    }
  }

  @Override
  public void updateFlagActiveDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgCodeId, boolean flagActive, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    debtPositionTypeOrgService.updateFlagActiveDebtPositionTypeOrg(debtPositionTypeOrgCodeId, flagActive, accessToken);
  }

  @Override
  public DebtPositionTypeOrg getDebtPositionTypeOrgByCode(Long organizationId, String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken) {
    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.findDebtPositionTypeOrg(organizationId, debtPositionTypeOrgCode, mappedExternalUserId, accessToken);
    if(debtPositionTypeOrg==null){
      throw new ResourceNotFoundException("DebtPositionTypeOrg with organizationId "+organizationId+" and code "+debtPositionTypeOrgCode+" not found");
    }
    return debtPositionTypeOrg;
  }
}
