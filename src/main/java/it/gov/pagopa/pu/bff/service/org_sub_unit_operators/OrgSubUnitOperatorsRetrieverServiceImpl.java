package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitOperatorsService;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.mapper.PagedOrgSubUnitOperatorsMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperatorsEmbedded;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrgSubUnitOperatorsRetrieverServiceImpl implements OrgSubUnitOperatorsRetrieverService {

  private final AuthorizationService authorizationService;
  private final OrgSubUnitOperatorsService orgSubUnitOperatorsService;
  private final PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapper;
  private final AuthzService authzService;

    public OrgSubUnitOperatorsRetrieverServiceImpl(AuthorizationService authorizationService, OrgSubUnitOperatorsService orgSubUnitOperatorsService, PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapper, AuthzService authzService) {
        this.authorizationService = authorizationService;
        this.orgSubUnitOperatorsService = orgSubUnitOperatorsService;
        this.pagedOrgSubUnitOperatorsMapper = pagedOrgSubUnitOperatorsMapper;
        this.authzService = authzService;
    }

  @Override
  public PagedOrgSubUnitOperators getOrgSubUnitOperators(Long organizationId, String subUnitCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    PagedModelOrgSubUnitOperators pagedModelOrgSubUnitOperators = orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, pageable, accessToken);

    List<OrgSubUnitOperators> orgSubUnitOperators = extractOperators(pagedModelOrgSubUnitOperators);

    List<OrgSubUnitOperator> content = orgSubUnitOperators.stream()
      .map(op -> enrichWithUserInfo(op, accessToken))
      .toList();

    return pagedOrgSubUnitOperatorsMapper.map(content, pagedModelOrgSubUnitOperators);
  }

  private List<OrgSubUnitOperators> extractOperators(PagedModelOrgSubUnitOperators paged) {
    return Optional.ofNullable(paged.getEmbedded())
      .map(PagedModelOrgSubUnitOperatorsEmbedded::getOrgSubUnitOperatorses)
      .orElse(Collections.emptyList());
  }

  private OrgSubUnitOperator enrichWithUserInfo(OrgSubUnitOperators sourceOperator, String accessToken) {
    UserInfo userInfo = null;
    try {
      userInfo = authzService.getUserInfoFromMappedExternaUserId(
        sourceOperator.getOperatorExternalUserId(), accessToken);
    } catch (Exception e) {
      log.warn("Error retrieving UserInfo for mappedExternalUserId={}",
        sourceOperator.getOperatorExternalUserId(), e);
    }
    return pagedOrgSubUnitOperatorsMapper.toOrgSubUnitOperator(sourceOperator, userInfo);
  }

}
