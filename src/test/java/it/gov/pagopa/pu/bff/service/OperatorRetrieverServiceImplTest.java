package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationOperatorMapper;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverService;
import it.gov.pagopa.pu.bff.service.operator.OperatorRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import org.junit.jupiter.api.AfterEach;
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

  private OperatorRetrieverService operatorRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    operatorRetrieverService = new OperatorRetrieverServiceImpl(
      authorizationServiceMock,authzServiceMock,debtPositionTypeOrgOperatorsServiceMock,pagedOrganizationOperatorMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            authorizationServiceMock,authzServiceMock,debtPositionTypeOrgOperatorsServiceMock,pagedOrganizationOperatorMapperMock
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
}
