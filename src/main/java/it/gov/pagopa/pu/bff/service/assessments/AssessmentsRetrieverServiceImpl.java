package it.gov.pagopa.pu.bff.service.assessments;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.exception.InvalidAssessmentsDetailException;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentExtendedDTOMapper;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRowsDetailMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssessmentsRetrieverServiceImpl implements AssessmentsRetrieverService {

  private final AssessmentsService assessmentsService;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final AssessmentExtendedDTOMapper assessmentExtendedDTOMapper;
  private final AssessmentsRowsDetailMapper assessmentsRowsDetailMapper;
  private final AuthzService authzService;

  public AssessmentsRetrieverServiceImpl(AssessmentsService assessmentsService,
                                         DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService,
                                         DebtPositionTypeOrgService debtPositionTypeOrgService,
                                         AssessmentExtendedDTOMapper assessmentExtendedDTOMapper,
                                         AssessmentsRowsDetailMapper assessmentsRowsDetailMapper,
                                         AuthzService authzService) {
    this.assessmentsService = assessmentsService;
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.assessmentExtendedDTOMapper = assessmentExtendedDTOMapper;
    this.assessmentsRowsDetailMapper = assessmentsRowsDetailMapper;
    this.authzService = authzService;
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

    PagedAssessmentsExtendedDTO result = assessmentExtendedDTOMapper.mapToPagedAssessmentsExtendedDTO(
      pagedAssessmentsView,
      debtPositionTypeOrgCode2DescriptionMap
    );

    Map<String, UserInfo> cache = new HashMap<>();

    for (AssessmentsExtendedDTO dto : result.getContent()) {
      String getOperatorExternalUserId = dto.getOperatorExternalUserId();

      UserInfo userInfo = cache.computeIfAbsent(getOperatorExternalUserId,
        operatorExternalUserId -> {
          try {
            return authzService.getUserInfoFromMappedExternaUserId(operatorExternalUserId, accessToken);
          } catch (Exception e) {
            log.warn("Error while retrieving UserInfo for {}", operatorExternalUserId, e);
            return null;
          }
        });

      if (userInfo != null) {
        dto.setName(userInfo.getName());
        dto.setFamilyName(userInfo.getFamilyName());
      }
    }

    return result;
  }

  private Map<String, String> getDebtPositionTypeOrgMap(Long organizationId, String mappedExternalUserId, String accessToken) {
    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, null, mappedExternalUserId, accessToken);

    if (debtPositionTypeOrgs != null
            && debtPositionTypeOrgs.getEmbedded() != null
            && !CollectionUtils.isEmpty(debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs())) {

      return debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream()
              .collect(Collectors.toMap(DebtPositionTypeOrg::getCode, DebtPositionTypeOrg::getDescription));
    } else {
      throw new NotFoundException("ASSESSMENT_NOT_FOUND", "Assessments not found for organizationId " + organizationId);
    }
  }

  private String getDebtPositionTypeOrgDescription(Long organizationId,String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken){
    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.findDebtPositionTypeOrg(organizationId, debtPositionTypeOrgCode, mappedExternalUserId, accessToken);

    if (debtPositionTypeOrg == null){
      throw new NotFoundException("DEBT_POSITION_TYPE_ORG_NOT_FOUND", "DebtPositionTypeOrg " + debtPositionTypeOrgCode + " not found for user " + mappedExternalUserId);
    }

    return debtPositionTypeOrg.getDescription();
  }

  @Override
  public AssessmentsRowsDetail getPagedAssessmentsRowsDetail(AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(assessmentsRowsDetailFiltersDTO.getOrganizationId(), loggedUser);

    DateUtils.validateDateFilters(assessmentsRowsDetailFiltersDTO.getUpdateDateTimeIntervalFilter(),"updateDateTime");
    DateUtils.validateDateFilters(assessmentsRowsDetailFiltersDTO.getPaymentDateTimeIntervalFilter(),"paymentDateTime");

    Assessments assessments = assessmentsService.getAssessmentsById(assessmentsRowsDetailFiltersDTO.getAssessmentId(), accessToken);
    if(assessments==null){
      throw new NotFoundException("ASSESSMENT_NOT_FOUND", "Assessment with id %s not found".formatted(assessmentsRowsDetailFiltersDTO.getAssessmentId()));
    }

    String debtPositionTypeOrgDescription = getDebtPositionTypeOrgDescription(assessmentsRowsDetailFiltersDTO.getOrganizationId(),
            assessments.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);

    AssessmentsRowsDetail result = assessmentsRowsDetailMapper.map(
      assessmentsService.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, pageable, accessToken),
      assessments,
      debtPositionTypeOrgDescription
    );

    enrichUserInfoByMappedExternalUserId(result.getUpdateOperatorExternalId(), accessToken,
      userInfo -> {
      result.setName(userInfo.getName());
      result.setFamilyName(userInfo.getFamilyName());
    });

    return result;
  }

  private void enrichUserInfoByMappedExternalUserId(String mappedExternalUserId, String accessToken, Consumer<UserInfo> userInfoConsumer) {
    if (StringUtils.isNotBlank(mappedExternalUserId)) {
      try {
        UserInfo info = authzService.getUserInfoFromMappedExternaUserId(mappedExternalUserId, accessToken);
        if (info != null) {
          userInfoConsumer.accept(info);
        }
      } catch (Exception e) {
        log.warn("Impossible retrieving UserInfo for mappedExternalUserId {}", mappedExternalUserId, e);
      }
    }
  }

  @Override
  public AssessmentsDetail getAssessmentsDetail(Long organizationId, Long assessmentId, Long assessmentDetailId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    AssessmentsDetail assessmentsDetail = assessmentsService.findAssessmentsDetail(assessmentDetailId, accessToken);
    if (assessmentsDetail != null && assessmentsDetail.getAssessmentId().equals(assessmentId)){
      return assessmentsDetail;
    }else {
      throw new InvalidAssessmentsDetailException("INVALID_ASSESSMENT_DETAIL", "The assessment detail with ID %s is either invalid or does not belong to the assessment with ID %s".formatted(assessmentDetailId, assessmentId));
    }

  }

  @Override
  public Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode,  UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    debtPositionTypeOrgRetrieverService.validateOperator(organizationId, debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);

    return assessmentsService.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, accessToken);
  }

  @Override
  public void updateAssessmentsStatus(Long organizationId, Long assessmentId, AssessmentStatus status, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,loggedUser);
    Assessments assessments = getManuallyGeneratedAssessment(organizationId, assessmentId, accessToken);
    if(!AuthorizationService.isAdminRole(organizationId,loggedUser) && !loggedUser.getMappedExternalUserId().equals(assessments.getOperatorExternalUserId())){
      throw new AuthorizationDeniedException("User is neither the organization’s admin nor the Assessments' creator");
    }
    assessmentsService.updateStatus(organizationId, assessmentId, status, accessToken);
  }

  private Assessments getManuallyGeneratedAssessment(Long organizationId, Long assessmentId, String accessToken) {
    Assessments assessments = assessmentsService.getAssessmentsById(assessmentId, accessToken);
    if(assessments == null || !organizationId.equals(assessments.getOrganizationId())){
      throw new NotFoundException("ASSESSMENT_NOT_FOUND", "Assessments having assessmentsId "+ assessmentId +" and organizationId "+ organizationId +" not found");
    } else if(!assessments.getFlagManualGeneration()){
      throw new IllegalArgumentException("Assessments having id "+ assessmentId +" has not been manually generated");
    }
    return assessments;
  }
}

