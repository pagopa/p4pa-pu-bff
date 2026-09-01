package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitOperatorsService;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.mapper.PagedOrgSubUnitOperatorsMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperatorsEmbedded;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private OrgSubUnitOperatorsService orgSubUnitOperatorsServiceMock;
  @Mock
  private PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapperMock;
  @Mock
  private AuthzService authzServiceMock;

  private static final Long ORGANIZATION_ID = 1L;
  private static final String SUB_UNIT_CODE = "subUnitCode";
  private static final String ACCESS_TOKEN = "accessToken";
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);
  private UserInfo loggedUser;
  @InjectMocks
  private OrgSubUnitOperatorsRetrieverServiceImpl service;

  @BeforeEach
  void setUp() {
    loggedUser = podamFactory.manufacturePojo(UserInfo.class);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, orgSubUnitOperatorsServiceMock, pagedOrgSubUnitOperatorsMapperMock);
  }

  @Test
  void getOrgSubUnitOperators_shouldPropagateException_whenAuthorizationFails() {
    doThrow(new RuntimeException("Not authorized"))
      .when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);

    assertThrows(RuntimeException.class, () ->
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, loggedUser, ACCESS_TOKEN));
  }

  @Test
  void getOrgSubUnitOperators_shouldReturnEmptyContent_whenEmbeddedIsNull() {
    PagedModelOrgSubUnitOperators pagedModel = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);
    pagedModel.setEmbedded(null);

    doNothing().when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCode(
      ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);
    when(pagedOrgSubUnitOperatorsMapperMock.map(Collections.emptyList(), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result =
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldReturnEmptyContent_whenOperatorsListIsEmpty() {
    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(Collections.emptyList());

    doNothing().when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCode(
      ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);
    when(pagedOrgSubUnitOperatorsMapperMock.map(Collections.emptyList(), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result =
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldEnrichEachOperator_withUserInfoFromAuthz() {
    OrgSubUnitOperators operator1 = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    OrgSubUnitOperators operator2 = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    List<OrgSubUnitOperators> sourceOperators = List.of(operator1, operator2);

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(sourceOperators);

    doNothing().when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCode(
      ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    UserInfo userInfo1 = podamFactory.manufacturePojo(UserInfo.class);
    UserInfo userInfo2 = podamFactory.manufacturePojo(UserInfo.class);
    when(authzServiceMock.getUserInfoFromMappedExternaUserId(operator1.getOperatorExternalUserId(), ACCESS_TOKEN))
      .thenReturn(userInfo1);
    when(authzServiceMock.getUserInfoFromMappedExternaUserId(operator2.getOperatorExternalUserId(), ACCESS_TOKEN))
      .thenReturn(userInfo2);

    OrgSubUnitOperator mapped1 = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    OrgSubUnitOperator mapped2 = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(operator1, userInfo1)).thenReturn(mapped1);
    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(operator2, userInfo2)).thenReturn(mapped2);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);
    List<OrgSubUnitOperator> expectedContent = List.of(mapped1, mapped2);
    when(pagedOrgSubUnitOperatorsMapperMock.map(expectedContent, pagedModel)).thenReturn(expectedResult);

    PagedOrgSubUnitOperators result =
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldPassNullUserInfo_whenAuthzServiceThrowsException() {
    OrgSubUnitOperators operator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(List.of(operator));

    doNothing().when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCode(
      ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    when(authzServiceMock.getUserInfoFromMappedExternaUserId(operator.getOperatorExternalUserId(), ACCESS_TOKEN))
      .thenThrow(new RuntimeException("Auth service unavailable"));

    OrgSubUnitOperator mappedWithNullUserInfo = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(operator, null))
      .thenReturn(mappedWithNullUserInfo);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);
    when(pagedOrgSubUnitOperatorsMapperMock.map(List.of(mappedWithNullUserInfo), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result =
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldContinueProcessingOtherOperators_whenOneAuthzCallFails() {
    OrgSubUnitOperators okOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    OrgSubUnitOperators failingOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    PagedModelOrgSubUnitOperators pagedModel =
      buildPagedModelWithEmbedded(List.of(okOperator, failingOperator));

    doNothing().when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCode(
      ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    UserInfo userInfo = podamFactory.manufacturePojo(UserInfo.class);
    when(authzServiceMock.getUserInfoFromMappedExternaUserId(okOperator.getOperatorExternalUserId(), ACCESS_TOKEN))
      .thenReturn(userInfo);
    when(authzServiceMock.getUserInfoFromMappedExternaUserId(failingOperator.getOperatorExternalUserId(), ACCESS_TOKEN))
      .thenThrow(new RuntimeException("Auth service unavailable"));

    OrgSubUnitOperator mappedOk = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    OrgSubUnitOperator mappedFailing = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(okOperator, userInfo)).thenReturn(mappedOk);
    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(failingOperator, null)).thenReturn(mappedFailing);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);
    List<OrgSubUnitOperator> expectedContent = List.of(mappedOk, mappedFailing);
    when(pagedOrgSubUnitOperatorsMapperMock.map(expectedContent, pagedModel)).thenReturn(expectedResult);

    PagedOrgSubUnitOperators result =
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  private PagedModelOrgSubUnitOperators buildPagedModelWithEmbedded(List<OrgSubUnitOperators> operators) {
    PagedModelOrgSubUnitOperators pagedModel = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);
    PagedModelOrgSubUnitOperatorsEmbedded embedded =
      podamFactory.manufacturePojo(PagedModelOrgSubUnitOperatorsEmbedded.class);
    embedded.setOrgSubUnitOperatorses(operators);
    pagedModel.setEmbedded(embedded);
    return pagedModel;
  }

}
