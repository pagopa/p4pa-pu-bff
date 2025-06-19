package it.gov.pagopa.pu.bff.service.assessments;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentExtendedDTOMapper;
import it.gov.pagopa.pu.bff.mapper.PagedAssessmentsRowsDetailMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
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

  private final AssessmentsService assessmentsService;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final AssessmentExtendedDTOMapper assessmentExtendedDTOMapper;
  private final PagedAssessmentsRowsDetailMapper pagedAssessmentsRowsDetailMapper;

  public AssessmentsRetrieverServiceImpl(AssessmentsService assessmentsService, DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentExtendedDTOMapper assessmentExtendedDTOMapper, PagedAssessmentsRowsDetailMapper pagedAssessmentsRowsDetailMapper) {
    this.assessmentsService = assessmentsService;
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.assessmentExtendedDTOMapper = assessmentExtendedDTOMapper;
    this.pagedAssessmentsRowsDetailMapper = pagedAssessmentsRowsDetailMapper;
  }

  @Override
  public PagedAssessmentsExtendedDTO getPagedAssessmentsExtendedDTO(AssessmentsFiltersDTO assessmentsFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(assessmentsFiltersDTO.getOrganizationId(), loggedUser);
    Map<String, String> debtPositionTypeOrgCode2DescriptionMap;

    if (StringUtils.isNotBlank(debtPositionTypeOrgCode)) {
      debtPositionTypeOrgRetrieverService.validateOperator(
        assessmentsFiltersDTO.getOrganizationId(),
        debtPositionTypeOrgCode,
        loggedUser.getMappedExternalUserId(),
        accessToken
      );

      String description = getDebtPositionTypeOrgDescription(
        assessmentsFiltersDTO.getOrganizationId(),
        debtPositionTypeOrgCode,
        loggedUser.getMappedExternalUserId(),
        accessToken
      );

      assessmentsFiltersDTO.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
      debtPositionTypeOrgCode2DescriptionMap = Map.of(debtPositionTypeOrgCode, description);

    } else {
      debtPositionTypeOrgCode2DescriptionMap = getDebtPositionTypeOrgMap(
        assessmentsFiltersDTO.getOrganizationId(),
        loggedUser.getMappedExternalUserId(),
        accessToken
      );
      assessmentsFiltersDTO.setDebtPositionTypeOrgCodes(debtPositionTypeOrgCode2DescriptionMap.keySet());
    }

    PagedAssessmentsView pagedAssessmentsView = assessmentsService.findPagedAssessmentsView(
      assessmentsFiltersDTO,
      pageable,
      accessToken
    );

    return assessmentExtendedDTOMapper.mapToPagedAssessmentsExtendedDTO(pagedAssessmentsView, debtPositionTypeOrgCode2DescriptionMap);
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

  private String getDebtPositionTypeOrgDescription(Long organizationId,String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken){
    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.findDebtPositionTypeOrg(organizationId, debtPositionTypeOrgCode, mappedExternalUserId, accessToken);

    if (debtPositionTypeOrg == null){
      throw new ResourceNotFoundException("DebtPositionTypeOrg " + debtPositionTypeOrgCode + " not found for user " + mappedExternalUserId);
    }

    return debtPositionTypeOrg.getDescription();
  }

  @Override
  public PagedAssessmentsRowsDetail getPagedAssessmentsRowsDetail(AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(assessmentsRowsDetailFiltersDTO.getOrganizationId(), loggedUser);

    DateUtils.validateDateFilters(assessmentsRowsDetailFiltersDTO.getUpdateDateTimeIntervalFilter(),"updateDateTime");
    DateUtils.validateDateFilters(assessmentsRowsDetailFiltersDTO.getPaymentDateTimeIntervalFilter(),"paymentDateTime");

    return pagedAssessmentsRowsDetailMapper.map(assessmentsService.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, pageable, accessToken));
  }

}

