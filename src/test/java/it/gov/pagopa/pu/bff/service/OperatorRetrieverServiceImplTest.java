package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OperatorDetailMapper;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationOperatorMapper;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverService;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperatorRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private AuthzService authzServiceMock;
  @Mock
  private DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsServiceMock;
  @Mock
  private PagedOrganizationOperatorMapper pagedOrganizationOperatorMapperMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private OperatorDetailMapper operatorDetailMapperMock;

  private OperatorRetrieverService operatorRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    operatorRetrieverService = new OperatorRetrieverServiceImpl(
      authorizationServiceMock,authzServiceMock,debtPositionTypeOrgOperatorsServiceMock,pagedOrganizationOperatorMapperMock, debtPositionTypeOrgServiceMock, operatorDetailMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            authorizationServiceMock,authzServiceMock,debtPositionTypeOrgOperatorsServiceMock,pagedOrganizationOperatorMapperMock, debtPositionTypeOrgServiceMock, operatorDetailMapperMock
    );
  }

  @Test
  void whenGetOrganizationOperatorsThenOk(){
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    UserOrganizationRoles userOrgRole = loggedUser.getOrganizations().getFirst();
    Long organizationId = 1L;
    userOrgRole.setOrganizationId(organizationId);
    String firstName = "firstName";
    String lastName = "lastName";
    String fiscalCode = "fiscalCode";
    Pageable pageable = PageRequest.of(0,20);
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    Map<String, DebtPositionTypeOrgOperatorsDptoCountView> dptoViewMap = new HashMap<>();
    for (OperatorDTO operator : operatorsPage.getContent()) {
      DebtPositionTypeOrgOperatorsDptoCountView dptooView = new DebtPositionTypeOrgOperatorsDptoCountView();
      dptooView.setOperatorExternalUserId(operator.getMappedExternalUserId());
      dptooView.setDebtPositionTypeOrgCount((long) dptoViewMap.size());
      dptoViewMap.put(operator.getMappedExternalUserId(),dptooView);
    }
    PagedOrganizationOperator expectedResult = new PagedOrganizationOperator();

    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    when(authzServiceMock.getOrganizationOperators(userOrgRole.getOrganizationIpaCode(),fiscalCode,firstName,lastName,pageable.getPageNumber(),pageable.getPageSize(),accessToken))
            .thenReturn(operatorsPage);
    when(debtPositionTypeOrgOperatorsServiceMock.findByOrganizationIdAndOperatorExternalUserIds(organizationId,dptoViewMap.keySet(),accessToken))
            .thenReturn(new ArrayList<>(dptoViewMap.values()));
    when(pagedOrganizationOperatorMapperMock.mapToPagedOrganizationOperator(operatorsPage,dptoViewMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().getDebtPositionTypeOrgCount()))))
            .thenReturn(expectedResult);

    PagedOrganizationOperator result = operatorRetrieverService.getOrganizationOperators(organizationId, firstName, lastName, fiscalCode, pageable, loggedUser, accessToken);

    assertEquals(expectedResult,result);
  }

  @Test
  void givenEmptyDptooViewWhenGetOrganizationOperatorsThenOk(){
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    UserOrganizationRoles userOrgRole = loggedUser.getOrganizations().getFirst();
    Long organizationId = 1L;
    userOrgRole.setOrganizationId(organizationId);
    String firstName = "firstName";
    String lastName = "lastName";
    String fiscalCode = "fiscalCode";
    Pageable pageable = PageRequest.of(0,20);
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    PagedOrganizationOperator expectedResult = new PagedOrganizationOperator();

    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    when(authzServiceMock.getOrganizationOperators(userOrgRole.getOrganizationIpaCode(),fiscalCode,firstName,lastName,pageable.getPageNumber(),pageable.getPageSize(),accessToken))
            .thenReturn(operatorsPage);
    when(debtPositionTypeOrgOperatorsServiceMock.findByOrganizationIdAndOperatorExternalUserIds(organizationId,operatorsPage.getContent().stream().map(OperatorDTO::getMappedExternalUserId).collect(Collectors.toSet()),accessToken))
            .thenReturn(Collections.emptyList());
    when(pagedOrganizationOperatorMapperMock.mapToPagedOrganizationOperator(operatorsPage,Collections.emptyMap()))
            .thenReturn(expectedResult);

    PagedOrganizationOperator result = operatorRetrieverService.getOrganizationOperators(organizationId, firstName, lastName, fiscalCode, pageable, loggedUser, accessToken);

    assertEquals(expectedResult,result);
  }

  @Test
  void givenEmptyOperatorsPageWhenGetOrganizationOperatorsThenReturnEmptyPage(){
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    UserOrganizationRoles userOrgRole = loggedUser.getOrganizations().getFirst();
    Long organizationId = 1L;
    userOrgRole.setOrganizationId(organizationId);
    String firstName = "firstName";
    String lastName = "lastName";
    String fiscalCode = "fiscalCode";
    Pageable pageable = PageRequest.of(0,20);
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    operatorsPage.setContent(Collections.emptyList());
    PagedOrganizationOperator expectedResult = new PagedOrganizationOperator();

    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    when(authzServiceMock.getOrganizationOperators(userOrgRole.getOrganizationIpaCode(),fiscalCode,firstName,lastName,pageable.getPageNumber(),pageable.getPageSize(),accessToken))
            .thenReturn(operatorsPage);
    when(pagedOrganizationOperatorMapperMock.mapToPagedOrganizationOperator(operatorsPage,Collections.emptyMap()))
            .thenReturn(expectedResult);

    PagedOrganizationOperator result = operatorRetrieverService.getOrganizationOperators(organizationId, firstName, lastName, fiscalCode, pageable, loggedUser, accessToken);

    assertEquals(expectedResult,result);
    verifyNoInteractions(debtPositionTypeOrgOperatorsServiceMock);
  }

  @Test
  void givenNoMatchingOrganizationWhenGetOrganizationOperatorsThenIllegalArgumentException(){
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    UserOrganizationRoles userOrgRole = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    Long organizationId = 1L;
    userOrgRole.setOrganizationId(organizationId+1);
    String firstName = "firstName";
    String lastName = "lastName";
    String fiscalCode = "fiscalCode";
    loggedUser.setOrganizations(List.of(userOrgRole));
    Pageable pageable = PageRequest.of(0,20);

    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);

    assertThrows(IllegalArgumentException.class,()-> operatorRetrieverService.getOrganizationOperators(organizationId, firstName, lastName, fiscalCode, pageable, loggedUser, accessToken));

    verifyNoInteractions(authzServiceMock,pagedOrganizationOperatorMapperMock,debtPositionTypeOrgOperatorsServiceMock);
  }

  @Test
  void givenParametersWhenFindPagedDebtPositionTypeOrgThenReturnOperatorsDetail() {
    //given
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    loggedUser.setUserId("user-123");
    String mappedExternalUserId = "mappedExternalUserId";
    loggedUser.setMappedExternalUserId(mappedExternalUserId);
    UserOrganizationRoles organizationRoles = loggedUser.getOrganizations().getFirst();
    organizationRoles.setOrganizationIpaCode("IPACODE");
    organizationRoles.setOrganizationId(organizationId);

    Long debtPositionTypeId = 1L;
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";
    OperatorDTO operatorDTO = new OperatorDTO();
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = new PagedModelDebtPositionTypeOrg();
    OperatorsDetail operatorsDetail = new OperatorsDetail();

    Mockito.when(authzServiceMock.getOrganizationOperator(organizationRoles.getOrganizationIpaCode(), loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(operatorDTO);
    Mockito.when(debtPositionTypeOrgServiceMock.findPagedDebtPositionTypeOrg(organizationId,loggedUser.getMappedExternalUserId(), debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionTypeId, Pageable.ofSize(1), accessToken)).thenReturn(pagedModelDebtPositionTypeOrg);
    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    Mockito.when(operatorDetailMapperMock.map(pagedModelDebtPositionTypeOrg, operatorDTO)).thenReturn(operatorsDetail);
    //when
    OperatorsDetail result = operatorRetrieverService.findPagedDebtPositionTypeOrg(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionTypeId, Pageable.ofSize(1), loggedUser, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(operatorsDetail, result);
  }

  @Test
  void givenOperatorNotFoundWhenFindPagedDebtPositionTypeOrgThenThrowResourceNotFoundException() {
    // given
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    loggedUser.setUserId("user-123");
    String mappedExternalUserId = "mappedExternalUserId";
    loggedUser.setMappedExternalUserId(mappedExternalUserId);
    UserOrganizationRoles organizationRoles = loggedUser.getOrganizations().getFirst();
    organizationRoles.setOrganizationIpaCode("IPACODE");
    organizationRoles.setOrganizationId(organizationId);

    String debtPositionTypeOrgCode = "code";
    String description = "description";
    Long debtPositionTypeId = 1L;
    Pageable pageable = Pageable.ofSize(1);

    Mockito.when(authzServiceMock.getOrganizationOperator(loggedUser.getOrganizations().getFirst().getOrganizationIpaCode(), loggedUser.getMappedExternalUserId(), accessToken))
      .thenReturn(null);
    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);

    ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
      operatorRetrieverService.findPagedDebtPositionTypeOrg(
        organizationId, mappedExternalUserId, debtPositionTypeOrgCode, description, debtPositionTypeId, pageable, loggedUser, accessToken
      )
    );
    Assertions.assertEquals("Operator not found for organization ipaCode IPACODE and userId mappedExternalUserId", ex.getMessage());
    Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock, operatorDetailMapperMock);
  }

  @Test
  void givenNoMatchingOrganizationWhenFindPagedDebtPositionTypeOrgThenIllegalArgumentException(){
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    UserOrganizationRoles userOrgRole = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    Long organizationId = 1L;
    userOrgRole.setOrganizationId(organizationId+1);

    String mappedExternalUserId = "mappedExternalUserId";
    loggedUser.setOrganizations(List.of(userOrgRole));
    Pageable pageable = PageRequest.of(0,20);
    String debtPositionTypeOrgCode = "code";
    String description = "description";
    Long debtPositionTypeId = 1L;

    doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);

    assertThrows(IllegalArgumentException.class,()-> operatorRetrieverService.findPagedDebtPositionTypeOrg(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, description, debtPositionTypeId, pageable, loggedUser, accessToken));

    verifyNoInteractions(authzServiceMock,debtPositionTypeOrgServiceMock, operatorDetailMapperMock);
  }
}
