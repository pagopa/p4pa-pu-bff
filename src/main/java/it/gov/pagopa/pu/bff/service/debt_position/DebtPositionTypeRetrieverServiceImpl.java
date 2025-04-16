package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.taxonomy.TaxonomyRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class DebtPositionTypeRetrieverServiceImpl implements
  DebtPositionTypeRetrieverService {

  private final AuthorizationService authorizationService;
  private final DebtPositionTypeService debtPositionTypeService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper;
  private final TaxonomyRetrieverService taxonomyRetrieverService;
  private final DebtPositionTypeMapper debtPositionTypeMapper;

  public DebtPositionTypeRetrieverServiceImpl(
    DebtPositionTypeService debtPositionTypeService,
    DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper,
    TaxonomyRetrieverService taxonomyRetrieverService,
    AuthorizationService authorizationService,
    DebtPositionTypeMapper debtPositionTypeMapper,
    DebtPositionTypeOrgService debtPositionTypeOrgService) {
    this.authorizationService = authorizationService;
    this.debtPositionTypeService = debtPositionTypeService;
    this.debtPositionTypeWithCountMapper = debtPositionTypeWithCountMapper;
    this.taxonomyRetrieverService = taxonomyRetrieverService;
    this.debtPositionTypeMapper = debtPositionTypeMapper;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
  }

  public DebtPositionType getDebtPositionTypeById(String accessToken, Long id) {
    return debtPositionTypeService.getDebtPositionTypeById(id, accessToken);
  }

  @Override
  public PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(
    Long organizationId, String description, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return debtPositionTypeWithCountMapper.mapToPagedDebtPositionWithCount(
      debtPositionTypeService.getDebtPositionTypeWithCount(
        loggedUser.getBrokerId(),
        description,
        pageable,
        accessToken)
    );
  }

  @Override
  public DebtPositionTypeDetailDTO getDebtPositionTypeDetail(
    Long organizationId, Long debtPositionTypeId, UserInfo loggedUser,
    String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);

    DebtPositionType debtPositionType = getDebtPositionTypeById(accessToken,
      debtPositionTypeId);
    if (debtPositionType == null ||
      !loggedUser.getBrokerId().equals(debtPositionType.getBrokerId()) ||
      StringUtils.isBlank(debtPositionType.getTaxonomyCode())) {
      return null;
    }

    Taxonomy taxonomy = taxonomyRetrieverService.getTaxonomyByTaxonomyCode(
      debtPositionType.getTaxonomyCode(), accessToken);
    if (taxonomy == null) {
      return null;
    }

    return debtPositionTypeMapper.mapToDebtPositionTypeDetailDTO(
      debtPositionType, taxonomy);
  }

  public DebtPositionType createDebtPositionType(
    DebtPositionTypeRequestBody debtPositionType, UserInfo loggedUser, String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);
    if (debtPositionType.getDebtPositionTypeId() != null) {
      throw new IllegalArgumentException("DebtPositionTypeId must be null");
    }
    return debtPositionTypeService.createDebtPositionType(debtPositionType, accessToken);
  }

  @Override
  public DebtPositionType patchDebtPositionType(
    Long debtPositionTypeId,
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody,
    UserInfo loggedUser,
    String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);
    return debtPositionTypeService.patchDebtPositionType(
      debtPositionTypeId,
      debtPositionTypeMapper.mapToDebtPositionTypeRequestBody(debtPositionTypePatchRequestBody),
      accessToken);
  }

  @Override
  public void deleteDebtPositionType(Long debtPositionTypeId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);

    PagedModelDebtPositionTypeOrg debtPositionTypeOrgs = debtPositionTypeOrgService.getDebtPositionTypeOrgByDebtPositionTypeId(
      debtPositionTypeId, PageRequest.of(0, 1), accessToken);

    if (debtPositionTypeOrgs != null && debtPositionTypeOrgs.getEmbedded() != null && !CollectionUtils.isEmpty(debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs())) {
      throw new ConflictException("Cannot delete DebtPositionType: it is associated with one or more DebtPositionTypeOrgs");
    }
    debtPositionTypeService.deleteDebtPositionType(debtPositionTypeId, accessToken);
  }

}
