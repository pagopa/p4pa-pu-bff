package it.gov.pagopa.pu.bff.service.assessments_registry;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsRegistryService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.exception.InvalidAssessmentsRegistryException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryDTOMapper;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.*;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssessmentsRegistryRetrieverServiceImpl implements AssessmentsRegistryRetrieverService {
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final AssessmentsRegistryService assessmentsRegistryService;
  private final AssessmentsRegistryMapper assessmentsRegistryMapper;
  private final AssessmentsRegistryDTOMapper assessmentsRegistryDTOMapper;

  public AssessmentsRegistryRetrieverServiceImpl(DebtPositionTypeOrgService debtPositionTypeOrgService,
                                                 DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService,
                                                 AssessmentsRegistryService assessmentsRegistryService,
                                                 AssessmentsRegistryMapper assessmentsRegistryMapper,
                                                 AssessmentsRegistryDTOMapper assessmentsRegistryDTOMapper) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
    this.assessmentsRegistryService = assessmentsRegistryService;
    this.assessmentsRegistryMapper = assessmentsRegistryMapper;
    this.assessmentsRegistryDTOMapper = assessmentsRegistryDTOMapper;
  }

  @Override
  public PagedAssessmentsRegistry getAssessmentsRegistries(AssessmentsRegistryFiltersDTO filters, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser);

    if (StringUtils.isNotBlank(debtPositionTypeOrgCode)) {
      debtPositionTypeOrgRetrieverService.validateOperator(filters.getOrganizationId(), debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
      filters.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
    } else {
      filters.setDebtPositionTypeOrgCodes(getDebtPositionTypeOrgCodes(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken));
    }
    return assessmentsRegistryMapper.mapToPagedAssessmentsRegistry(
      assessmentsRegistryService.findAssessmentsRegistriesByFilters(filters, pageable, accessToken));
  }

  private Set<String> getDebtPositionTypeOrgCodes(Long organizationId, String mappedExternalUserId, String accessToken) {
    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, mappedExternalUserId, accessToken);
    if (debtPositionTypeOrgs != null
      && debtPositionTypeOrgs.getEmbedded() != null
      && !CollectionUtils.isEmpty(debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs())) {
      return debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());
    } else {
      throw new ResourceNotFoundException("AssessmentsRegistries not found for organizationId " + organizationId);
    }
  }

  @Override
  public AssessmentsRegistryDTO getAssessmentsRegistry(Long organizationId, Long assessmentRegistryId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    AssessmentsRegistryDTO assessmentRegistryDTO = assessmentsRegistryDTOMapper.map(assessmentsRegistryService.getAssessmentsRegistry(assessmentRegistryId, accessToken));
    debtPositionTypeOrgRetrieverService.validateOperator(organizationId, assessmentRegistryDTO.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);

    return assessmentRegistryDTO;
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
  public AssessmentsRegistry updateAssessmentsRegistry(Long organizationId, AssessmentsRegistry body, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
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
