package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sub_unit.OrgSubUnitRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.SubUnitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorControllerTest {

  @Mock
  private OperatorRetrieverService operatorRetrieverServiceMock;
  @Mock
  private OrgSubUnitRetrieverService orgSubUnitRetrieverServiceMock;

  @InjectMocks
  private OperatorController operatorController;

  private final String accessToken = "fakeAccessToken";
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      operatorRetrieverServiceMock,
      orgSubUnitRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenGetOrganizationOperatorsThenOk() {
    long organizationId = 1L;
    String firstName = "firstName";
    String lastName = "lastName";
    String fiscalCode = "fiscalCode";
    Pageable pageable = PageRequest.of(0,20);

    PagedOrganizationOperator expectedResult = new PagedOrganizationOperator();

    Mockito.when(operatorRetrieverServiceMock.getOrganizationOperators(
      organizationId,firstName, lastName, fiscalCode, pageable,loggedUser,accessToken)).thenReturn(expectedResult);

    ResponseEntity<PagedOrganizationOperator> response = operatorController.getOrganizationOperators(organizationId, firstName, lastName, fiscalCode, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenParametersWhenGetOperatorDetailsThenReturnOperatorsDetail() {
    //given
    Long organizationId = 1L;
    Long debtPositionId = 1L;
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";
    String mappedExternalUserId = "mappedExternalUserId";
    OperatorsDetail expectedResult = new OperatorsDetail();

    OperatorDetailsFiltersDTO operatorDetailsFiltersDTO = new OperatorDetailsFiltersDTO(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId);
    Mockito.when(operatorRetrieverServiceMock.getOperatorDetails(operatorDetailsFiltersDTO, Pageable.ofSize(1), loggedUser, accessToken)).thenReturn(expectedResult);
    //when
    ResponseEntity<OperatorsDetail> result = operatorController.getOperatorDetails(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId, Pageable.ofSize(1));
    //then
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(expectedResult, result.getBody());
  }

  @Test
  void whenRemoveDebtPositionTypeOrgFromOperatorThenOk() {
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 10L;
    String mappedExternalUserId = "user1";
    int expectedDeleted = 2;

    Mockito.when(operatorRetrieverServiceMock.removeDebtPositionTypeOrgFromOperator(organizationId, mappedExternalUserId, debtPositionTypeOrgId, loggedUser, accessToken))
      .thenReturn(expectedDeleted);

    ResponseEntity<Integer> response = operatorController.removeDebtPositionTypeOrgFromOperator(organizationId, mappedExternalUserId, debtPositionTypeOrgId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(expectedDeleted, response.getBody());
  }

  @Test
  void whenGetDebtPositionTypeOrgsNotEnabledForOperatorThenReturnOperatorsDetail() {
    //given
    Long organizationId = 1L;
    Long debtPositionId = 1L;
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";
    String mappedExternalUserId = "mappedExternalUserId";
    PagedDebtPositionTypeOrgDTO expectedResult = new PagedDebtPositionTypeOrgDTO();

    OperatorDetailsFiltersDTO operatorDetailsFiltersDTO = new OperatorDetailsFiltersDTO(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId);
    Mockito.when(operatorRetrieverServiceMock.getDebtPositionTypeOrgsNotEnabledForOperator(operatorDetailsFiltersDTO, Pageable.ofSize(1), loggedUser, accessToken)).thenReturn(expectedResult);
    //when
    ResponseEntity<PagedDebtPositionTypeOrgDTO> result = operatorController.getDebtPositionTypeOrgsNotEnabledForOperator(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId, Pageable.ofSize(1));
    //then
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(expectedResult, result.getBody());
  }

  @Test
  void whenSaveDebtPositionTypeOrgOperatorsForOperatorThenCreated() {
    Long organizationId = 1L;
    String mappedExternalUserId = "user123";
    Set<Long> debtPositionTypeOrgIds = Set.of(100L, 200L);

    ResponseEntity<Void> response = operatorController.enableDebtPositionTypeOrgsForOperator(
      organizationId, mappedExternalUserId, debtPositionTypeOrgIds);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNull(response.getBody());

    Mockito.verify(operatorRetrieverServiceMock).enableDebtPositionTypeOrgsForOperator(
      organizationId,
      mappedExternalUserId,
      debtPositionTypeOrgIds,
      loggedUser,
      accessToken);
  }

  @Test
  void whenGetOperatorOrgSubUnitsThenOk() {
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus status = OrgSubUnitStatus.ACTIVE;
    SubUnitType subUnitType = SubUnitType.UO;

    Pageable pageable = PageRequest.ofSize(10);

    PagedOrgSubUnit expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnit.class);

    when(
      orgSubUnitRetrieverServiceMock.getPagedOrgSubUnits(
        Mockito.any(PagedOrgSubUnitFiltersDTO.class),
        Mockito.eq(pageable),
        Mockito.eq(loggedUser),
        Mockito.eq(accessToken)
      )
    ).thenReturn(expectedResult);

    ResponseEntity<PagedOrgSubUnit> response = operatorController.getOperatorOrgSubUnits(
      organizationId,
      mappedExternalUserId,
      subUnitCode,
      status,
      subUnitType,
      pageable
    );

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}
