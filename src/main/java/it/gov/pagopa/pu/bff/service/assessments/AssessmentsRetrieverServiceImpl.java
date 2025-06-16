package it.gov.pagopa.pu.bff.service.assessments;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedAssessmentExtendedDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssessmentsRetrieverServiceImpl implements AssessmentsRetrieverService {

  private final AssessmentsClient assessmentsClient;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final PagedAssessmentExtendedDTOMapper pagedAssessmentExtendedDTOMapper;

  public AssessmentsRetrieverServiceImpl(AssessmentsClient assessmentsClient, DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService, DebtPositionTypeOrgService debtPositionTypeOrgService, PagedAssessmentExtendedDTOMapper pagedAssessmentExtendedDTOMapper) {
    this.assessmentsClient = assessmentsClient;
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
      this.debtPositionTypeOrgService = debtPositionTypeOrgService;
      this.pagedAssessmentExtendedDTOMapper = pagedAssessmentExtendedDTOMapper;
  }

  @Override
  public PagedAssessmentsExtendedDTO getPagedAssessmentsExtendedDTO(AssessmentsFiltersDTO assessmentsFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(assessmentsFiltersDTO.getOrganizationId(), loggedUser);
    Map<String, String> debtPositionTypeOrgMap;

    if (StringUtils.isNotBlank(debtPositionTypeOrgCode)){
      debtPositionTypeOrgRetrieverService.validateOperator(assessmentsFiltersDTO.getOrganizationId(), debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
      assessmentsFiltersDTO.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));

      String description = getDebtPositionTypeOrgMap(
              assessmentsFiltersDTO.getOrganizationId(),
              loggedUser.getMappedExternalUserId(),
              accessToken
      ).getOrDefault(debtPositionTypeOrgCode, "");

      debtPositionTypeOrgMap = Map.of(debtPositionTypeOrgCode, description);
    }else{
      debtPositionTypeOrgMap = getDebtPositionTypeOrgMap(
              assessmentsFiltersDTO.getOrganizationId(),
              loggedUser.getMappedExternalUserId(),
              accessToken
      );
      assessmentsFiltersDTO.setDebtPositionTypeOrgCodes(debtPositionTypeOrgMap.keySet());
    }

    PagedAssessmentsView pagedAssessmentsView = assessmentsClient.findPagedAssessmentsView(
            assessmentsFiltersDTO,
            pageable,
            accessToken
    );
    return pagedAssessmentExtendedDTOMapper.map(pagedAssessmentsView, debtPositionTypeOrgMap);

  }

  private Map<String, String> getDebtPositionTypeOrgMap(Long organizationId, String mappedExternalUserId, String accessToken) {
    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, mappedExternalUserId, accessToken);

    if (debtPositionTypeOrgs != null
            && debtPositionTypeOrgs.getEmbedded() != null
            && !CollectionUtils.isEmpty(debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs())) {

      return debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream()
              .collect(Collectors.toMap(DebtPositionTypeOrg::getCode, DebtPositionTypeOrg::getDescription));
    } else {
      throw new ResourceNotFoundException("Assessments not found for organizationId " + organizationId);
    }
  }

}

