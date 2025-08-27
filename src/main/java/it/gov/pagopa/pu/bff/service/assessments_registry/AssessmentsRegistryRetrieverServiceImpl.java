package it.gov.pagopa.pu.bff.service.assessments_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsRegistryService;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.exception.InvalidAssessmentsRegistryException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryDTOMapper;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryExtendedDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistryEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssessmentsRegistryRetrieverServiceImpl implements AssessmentsRegistryRetrieverService {
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final AssessmentsRegistryService assessmentsRegistryService;
  private final AssessmentsRegistryExtendedDTOMapper assessmentsRegistryExtendedDTOMapper;
  private final AssessmentsRegistryDTOMapper assessmentsRegistryDTOMapper;

  public AssessmentsRegistryRetrieverServiceImpl(DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService,
                                                 AssessmentsRegistryService assessmentsRegistryService,
                                                 AssessmentsRegistryExtendedDTOMapper assessmentsRegistryExtendedDTOMapper,
                                                 AssessmentsRegistryDTOMapper assessmentsRegistryDTOMapper) {
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
    this.assessmentsRegistryService = assessmentsRegistryService;
    this.assessmentsRegistryExtendedDTOMapper = assessmentsRegistryExtendedDTOMapper;
    this.assessmentsRegistryDTOMapper = assessmentsRegistryDTOMapper;
  }

  @Override
  public PagedAssessmentsRegistry getAssessmentsRegistries(AssessmentsRegistryFiltersDTO filters, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser);
    Map<String, DebtPositionTypeOrg> debtPositionTypeOrgCodeMap = getDebtPositionTypeOrgCodeMap(filters, debtPositionTypeOrgCode, loggedUser, accessToken);
    filters.setDebtPositionTypeOrgCodes(debtPositionTypeOrgCodeMap.keySet());
    return assessmentsRegistryExtendedDTOMapper.mapToPagedAssessmentsRegistry(
      assessmentsRegistryService.findAssessmentsRegistriesByFilters(filters, pageable, accessToken),
            debtPositionTypeOrgCodeMap);
  }

  private Map<String, DebtPositionTypeOrg> getDebtPositionTypeOrgCodeMap(AssessmentsRegistryFiltersDTO filters, String debtPositionTypeOrgCode, UserInfo loggedUser, String accessToken) {
    Map<String,DebtPositionTypeOrg> debtPositionTypeOrgMap = new HashMap<>();
    if (StringUtils.isNotBlank(debtPositionTypeOrgCode)) {
      DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgByCode(filters.getOrganizationId(), debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
      debtPositionTypeOrgMap.put(debtPositionTypeOrgCode,debtPositionTypeOrg);
    } else {
      List<DebtPositionTypeOrg> debtPositionTypeOrgs = getDebtPositionTypeOrgs(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken);
      debtPositionTypeOrgMap = debtPositionTypeOrgs.stream().collect(Collectors.toMap(DebtPositionTypeOrg::getCode, Function.identity()));
    }
    return debtPositionTypeOrgMap;
  }

  private List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, String mappedExternalUserId, String accessToken) {
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgs(organizationId, null, mappedExternalUserId, accessToken);
    if(CollectionUtils.isEmpty(debtPositionTypeOrgs)){
      throw new ResourceNotFoundException("AssessmentsRegistries not found for organizationId " + organizationId);
    }
    return debtPositionTypeOrgs;
  }

  @Override
  public AssessmentsRegistryDTO getAssessmentsRegistry(Long organizationId, Long assessmentRegistryId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryService.getAssessmentsRegistry(assessmentRegistryId, accessToken);
    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgByCode(organizationId, assessmentsRegistry.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
    return assessmentsRegistryDTOMapper.map(assessmentsRegistry,debtPositionTypeOrg.getDescription());
  }

  @Override
  public AssessmentsRegistry createAssessmentsRegistry(Long organizationId, AssessmentsRegistry assessmentsRegistry, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    if (assessmentsRegistry.getAssessmentRegistryId() != null) {
      throw new InvalidAssessmentsRegistryException("assessmentRegistryId should not be provided");
    }
    validateAssessmentRegistry(organizationId, assessmentsRegistry, loggedUser.getMappedExternalUserId(), accessToken);
    assessmentsRegistry.setStatus(AssessmentsRegistryStatus.ACTIVE);
    return assessmentsRegistryService.createAssessmentsRegistry(assessmentsRegistry, accessToken);
  }

  @Override
  public AssessmentsRegistry updateAssessmentsRegistry(Long organizationId,  Long assessmentRegistryId, AssessmentsRegistry body, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    if (!Objects.equals(assessmentRegistryId, body.getAssessmentRegistryId())) {
      throw new IllegalArgumentException("assessmentRegistryId in path and body must match");
    }

    AssessmentsRegistry existingRegistry = assessmentsRegistryService.getAssessmentsRegistry(body.getAssessmentRegistryId(), accessToken);

    if (!Objects.equals(body.getDebtPositionTypeOrgCode(), existingRegistry.getDebtPositionTypeOrgCode())) {
      throw new IllegalArgumentException("debtPositionTypeOrgCode cannot be modified");
    }

    validateAssessmentRegistry(organizationId, body, loggedUser.getMappedExternalUserId(), accessToken);

    if (AssessmentsRegistryStatus.ACTIVE.equals(body.getStatus())) {
      checkActiveRegistryUniqueness(organizationId, body, accessToken);
    }
    return assessmentsRegistryService.updateAssessmentsRegistry(body, accessToken);
  }

  private void validateAssessmentRegistry(Long organizationId, AssessmentsRegistry assessmentsRegistry, String mappedExternalUserId, String accessToken) {
    if (!organizationId.equals(assessmentsRegistry.getOrganizationId())) {
      throw new InvalidAssessmentsRegistryException("The AssessmentsRegistry's organizationId " + assessmentsRegistry.getOrganizationId() +
        " does not match the given organizationId " + organizationId);
    }
    debtPositionTypeOrgRetrieverService.validateOperator(assessmentsRegistry.getOrganizationId(), assessmentsRegistry.getDebtPositionTypeOrgCode(), mappedExternalUserId, accessToken);
  }

  private void checkActiveRegistryUniqueness(Long organizationId, AssessmentsRegistry body, String accessToken) {
    AssessmentsRegistryFiltersDTO filters = new AssessmentsRegistryFiltersDTO();
    filters.setOrganizationId(organizationId);
    filters.setDebtPositionTypeOrgCodes(Set.of(body.getDebtPositionTypeOrgCode()));
    filters.setOperatingYear(body.getOperatingYear());
    filters.setStatus(AssessmentsRegistryStatus.ACTIVE);

    Pageable minimalPage = PageRequest.of(0, 1);
    PagedModelAssessmentsRegistry result = assessmentsRegistryService.findAssessmentsRegistriesByFilters(filters, minimalPage, accessToken);

    List<AssessmentsRegistry> registries = Optional.ofNullable(result.getEmbedded())
      .map(PagedModelAssessmentsRegistryEmbedded::getAssessmentsRegistries)
      .orElse(Collections.emptyList());

    boolean conflictExists = registries.stream()
      .anyMatch(r -> !Objects.equals(r.getAssessmentRegistryId(), body.getAssessmentRegistryId()));

    boolean tooManyActive = result.getPage() != null && result.getPage().getTotalElements() >= 2;

    if (conflictExists || tooManyActive) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
        "There is already a registry with status ACTIVE having the same debtPositionTypeOrgCode and operatingYear");
    }
  }
}
