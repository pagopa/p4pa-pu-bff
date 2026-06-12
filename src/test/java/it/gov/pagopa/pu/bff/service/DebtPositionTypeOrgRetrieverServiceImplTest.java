package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.*;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionTypeOrgException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgDTOMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgOperatorsMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgWithCountMapper;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost.DebtPositionTypeOrgBalanceCostRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsServiceMock;
  @Mock
  private DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapperMock;
  @Mock
  private DebtPositionTypeOrgOperatorsMapper debtPositionTypeOrgOperatorsMapperMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private DebtPositionService debtPositionServiceMock;
  @Mock
  private AuthzService authzServiceMock;
  @Mock
  private DebtPositionTypeService debtPositionTypeServiceMock;
  @Mock
  private DebtPositionTypeOrgMapper debtPositionTypeOrgMapperMock;
  @Mock
  private DebtPositionTypeOrgDTOMapper debtPositionTypeOrgMapperDTOMock;
  @Mock
  private OrgSilServiceRetrieverService orgSilServiceRetrieverServiceMock;
  @Mock
  private SpontaneousFormRetrieverService spontaneousFormRetrieverServiceMock;
  @Mock
  private DebtPositionTypeOrgBalanceCostRetrieverService debtPositionTypeOrgBalanceCostRetrieverServiceMock;

  private DebtPositionTypeOrgRetrieverServiceImpl debtPositionTypeOrgService;

  private final String accessToken = "TOKEN";

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgService = new DebtPositionTypeOrgRetrieverServiceImpl(
      debtPositionTypeOrgServiceMock, debtPositionTypeOrgOperatorsServiceMock, debtPositionServiceMock,
      authorizationServiceMock, authzServiceMock, debtPositionTypeServiceMock, debtPositionTypeOrgWithCountMapperMock,
      debtPositionTypeOrgOperatorsMapperMock, debtPositionTypeOrgMapperMock, debtPositionTypeOrgMapperDTOMock,
      orgSilServiceRetrieverServiceMock, spontaneousFormRetrieverServiceMock, debtPositionTypeOrgBalanceCostRetrieverServiceMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgServiceMock, debtPositionTypeOrgOperatorsServiceMock,
      debtPositionServiceMock, authorizationServiceMock, authzServiceMock, debtPositionTypeServiceMock, debtPositionTypeOrgWithCountMapperMock,
      debtPositionTypeOrgOperatorsMapperMock, debtPositionTypeOrgMapperMock, debtPositionTypeOrgMapperDTOMock, orgSilServiceRetrieverServiceMock, spontaneousFormRetrieverServiceMock);
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeOrgByIdThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    long debtPositionTypeId = 10L;
    long notifyServiceId = 200L;
    long amountServiceId = 300L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setOrganizationId(1L);
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setNotifyOutcomePushOrgSilServiceId(notifyServiceId);
    debtPositionTypeOrg.setAmountActualizationOrgSilServiceId(amountServiceId);
    debtPositionTypeOrg.setSpontaneousFormId(1L);

    DebtPositionType debtPositionType = new DebtPositionType();
    debtPositionType.setDescription("Description");
    debtPositionType.setCode("Code");

    SpontaneousForm spontaneousForm = new SpontaneousForm();
    spontaneousForm.setOrganizationId(1L);
    spontaneousForm.setSpontaneousFormId(1L);
    spontaneousForm.setCode("spontaneousFormCode");

    DebtPositionTypeOrgBalanceCostDTO dptobc = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrgBalanceCostDTO.class);
    List<DebtPositionTypeOrgBalanceCostDTO> dptobcList = List.of(dptobc);

    DebtPositionTypeOrgDTO expectedResult = new DebtPositionTypeOrgDTO();
    expectedResult.setDebtPositionTypeDescription("Description");
    expectedResult.setDebtPositionTypeCode("Code");
    expectedResult.setNotifyOutcomePushOrgSilServiceApplicationName("NotifyApp");
    expectedResult.setAmountActualizationOrgSilServiceApplicationName("AmountApp");
    expectedResult.setSpontaneousFormCode(spontaneousForm.getCode());
    expectedResult.setDebtPositionTypeOrgBalanceCosts(dptobcList);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic
        .when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken))
        .thenReturn(debtPositionTypeOrg);
      Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(debtPositionTypeId, accessToken))
        .thenReturn(debtPositionType);
      Mockito.when(orgSilServiceRetrieverServiceMock.getOrgSilServiceApplicationName(notifyServiceId, accessToken))
        .thenReturn("NotifyApp");
      Mockito.when(orgSilServiceRetrieverServiceMock.getOrgSilServiceApplicationName(amountServiceId, accessToken))
        .thenReturn("AmountApp");
      Mockito.when(debtPositionTypeOrgBalanceCostRetrieverServiceMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(
        Mockito.eq(debtPositionTypeOrgId),
        Mockito.anyString(),
        Mockito.eq(accessToken)
      )).thenReturn(dptobcList);
      Mockito.when(debtPositionTypeOrgMapperDTOMock.map(debtPositionTypeOrg, debtPositionType, "NotifyApp", "AmountApp", spontaneousForm.getCode(), dptobcList))
        .thenReturn(expectedResult);
      Mockito.when(spontaneousFormRetrieverServiceMock.getSpontaneousFormAndValidate(debtPositionTypeOrg.getSpontaneousFormId(), debtPositionTypeOrg, accessToken))
        .thenReturn(spontaneousForm);

      DebtPositionTypeOrgDTO result = debtPositionTypeOrgService.getDebtPositionTypeOrgById(
        organizationId, debtPositionTypeOrgId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
      assertEquals("Description", result.getDebtPositionTypeDescription());
      assertEquals("Code", result.getDebtPositionTypeCode());
      assertEquals("NotifyApp", result.getNotifyOutcomePushOrgSilServiceApplicationName());
      assertEquals("AmountApp", result.getAmountActualizationOrgSilServiceApplicationName());
      assertEquals(spontaneousForm.getCode(), result.getSpontaneousFormCode());
      assertEquals(dptobcList, result.getDebtPositionTypeOrgBalanceCosts());
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNullDebtPositionTypeOrgWhenGetDebtPositionTypeOrgByIdThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken))
        .thenReturn(null);

      assertThrows(ResourceNotFoundException.class, () -> debtPositionTypeOrgService.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNoDebtPositionTypeWhenGetByIdThenReturnWithNullDebtPositionTypeFields() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    long debtPositionTypeId = 10L;
    long notifyServiceId = 200L;
    long amountServiceId = 300L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setNotifyOutcomePushOrgSilServiceId(notifyServiceId);
    debtPositionTypeOrg.setAmountActualizationOrgSilServiceId(amountServiceId);

    DebtPositionTypeOrgDTO expectedDTO = new DebtPositionTypeOrgDTO();
    expectedDTO.setDebtPositionTypeDescription(null);
    expectedDTO.setDebtPositionTypeCode(null);
    expectedDTO.setNotifyOutcomePushOrgSilServiceApplicationName(null);
    expectedDTO.setAmountActualizationOrgSilServiceApplicationName(null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic
        .when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken))
        .thenReturn(debtPositionTypeOrg);
      Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(debtPositionTypeId, accessToken))
        .thenReturn(null);
      Mockito.when(orgSilServiceRetrieverServiceMock.getOrgSilServiceApplicationName(notifyServiceId, accessToken))
        .thenReturn(null);
      Mockito.when(orgSilServiceRetrieverServiceMock.getOrgSilServiceApplicationName(amountServiceId, accessToken))
        .thenReturn(null);
      Mockito.when(debtPositionTypeOrgMapperDTOMock.map(debtPositionTypeOrg, null, null, null, null, Collections.emptyList()))
        .thenReturn(expectedDTO);

      DebtPositionTypeOrgDTO result = debtPositionTypeOrgService.getDebtPositionTypeOrgById(
        organizationId, debtPositionTypeOrgId, loggedUser, accessToken);

      assertNotNull(result);
      assertNull(result.getDebtPositionTypeDescription());
      assertNull(result.getDebtPositionTypeCode());
      assertNull(result.getNotifyOutcomePushOrgSilServiceApplicationName());
      assertNull(result.getAmountActualizationOrgSilServiceApplicationName());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(spontaneousFormRetrieverServiceMock);
    }
  }

  @Test
  void givenNullServiceIdsWhenGetByIdThenReturnWithNullServiceApplicationNames() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    long debtPositionTypeId = 10L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setNotifyOutcomePushOrgSilServiceId(null);
    debtPositionTypeOrg.setAmountActualizationOrgSilServiceId(null);

    DebtPositionType debtPositionType = new DebtPositionType();
    debtPositionType.setDescription("Description");
    debtPositionType.setCode("Code");

    DebtPositionTypeOrgDTO expectedDTO = new DebtPositionTypeOrgDTO();
    expectedDTO.setDebtPositionTypeDescription("Description");
    expectedDTO.setDebtPositionTypeCode("Code");
    expectedDTO.setNotifyOutcomePushOrgSilServiceApplicationName(null);
    expectedDTO.setAmountActualizationOrgSilServiceApplicationName(null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic
        .when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken))
        .thenReturn(debtPositionTypeOrg);
      Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(debtPositionTypeId, accessToken))
        .thenReturn(debtPositionType);
      Mockito.when(orgSilServiceRetrieverServiceMock.getOrgSilServiceApplicationName(null, accessToken))
        .thenReturn(null);
      Mockito.when(debtPositionTypeOrgMapperDTOMock.map(debtPositionTypeOrg, debtPositionType, null, null, null, Collections.emptyList()))
        .thenReturn(expectedDTO);

      DebtPositionTypeOrgDTO result = debtPositionTypeOrgService.getDebtPositionTypeOrgById(
        organizationId, debtPositionTypeOrgId, loggedUser, accessToken);

      assertNotNull(result);
      assertEquals("Description", result.getDebtPositionTypeDescription());
      assertEquals("Code", result.getDebtPositionTypeCode());
      assertNull(result.getNotifyOutcomePushOrgSilServiceApplicationName());
      assertNull(result.getAmountActualizationOrgSilServiceApplicationName());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(spontaneousFormRetrieverServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeOrgByIdThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionTypeOrgService.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
    }
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeOrgsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    PagedModelDebtPositionTypeOrgEmbedded embedded = new PagedModelDebtPositionTypeOrgEmbedded();
    embedded.setDebtPositionTypeOrgs(List.of(debtPositionTypeOrg));
    CollectionModelDebtPositionTypeOrg collectionModel = new CollectionModelDebtPositionTypeOrg();
    collectionModel.setEmbedded(embedded);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(collectionModel);

      List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, true, loggedUser, accessToken);

      assertNotNull(result);
      assertFalse(result.isEmpty());
      assertSame(debtPositionTypeOrg, result.get(0));
      assertEquals(embedded.getDebtPositionTypeOrgs().size(), result.size());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }


  @Test
  void givenEmptyResultWhenGetDebtPositionTypeOrgsThenEmptyList() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    CollectionModelDebtPositionTypeOrg emptyCollectionModel = new CollectionModelDebtPositionTypeOrg();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(emptyCollectionModel);

      List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, true, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(result.isEmpty());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }


  @Test
  void givenInvalidUserWhenGetDebtPositionTypeOrgsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, true, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidAdminWhenGetDebtPositionTypeOrgWithCountThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");

    long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);
    PagedModelDebtPositionTypeOrgWithCount pagedModelDebtPositionTypeOrgWithCount = new PagedModelDebtPositionTypeOrgWithCount();
    PagedDebtPositionTypeOrgWithCount mappedDebtPositionTypeOrgs = new PagedDebtPositionTypeOrgWithCount();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgWithCount(organizationId, code, description, true, pageable, accessToken))
      .thenReturn(pagedModelDebtPositionTypeOrgWithCount);

    Mockito.when(debtPositionTypeOrgWithCountMapperMock.mapToPagedDebtPositionTypeOrgWithCount(pagedModelDebtPositionTypeOrgWithCount))
      .thenReturn(mappedDebtPositionTypeOrgs);

    PagedDebtPositionTypeOrgWithCount result = debtPositionTypeOrgService.getDebtPositionTypeOrgWithCount(organizationId, code, description, true, pageable, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(mappedDebtPositionTypeOrgs, result);
  }

  @Test
  void givenNonAdminWhenGetDebtPositionTypeOrgWithCountThenUnauthorized() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-456");

    long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);

    Mockito.doThrow(new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId()))
      .when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    assertThrows(AuthorizationDeniedException.class, () -> debtPositionTypeOrgService.getDebtPositionTypeOrgWithCount(organizationId, code, description, true, pageable, loggedUser, accessToken));
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeOrgOperatorsThenOk() {
    long organizationId = 1L;
    String organizationIpaCode = "ipaCode";
    long debtPositionTypeOrgId = 1L;

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.getOrganizations()
      .add(podamFactory.manufacturePojo(UserOrganizationRoles.class).toBuilder()
        .organizationId(organizationId)
        .organizationIpaCode(organizationIpaCode)
        .build());

    OperatorsPage operatorsPage = podamFactory.manufacturePojo(
      OperatorsPage.class);
    CollectionModelDebtPositionTypeOrgOperators collectionModelDebtPositionTypeOrgOperators = podamFactory.manufacturePojo(
      CollectionModelDebtPositionTypeOrgOperators.class);

    PagedDebtPositionTypeOrgOperatorDTO expectedResult = new PagedDebtPositionTypeOrgOperatorDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser)).thenAnswer(a -> null);

      Mockito.when(
          authzServiceMock.getOrganizationOperators(organizationIpaCode, null,
            null, null, 0, 10, accessToken))
        .thenReturn(operatorsPage);
      Mockito.when(
          debtPositionTypeOrgOperatorsServiceMock.getDebtPositionTypeOrgOperators(
            debtPositionTypeOrgId, accessToken))
        .thenReturn(collectionModelDebtPositionTypeOrgOperators);
      Mockito.when(
          debtPositionTypeOrgOperatorsMapperMock.mapToPagedDebtPositionTypeOrgOperatorDTO(
            operatorsPage, collectionModelDebtPositionTypeOrgOperators))
        .thenReturn(expectedResult);

      PagedDebtPositionTypeOrgOperatorDTO result = debtPositionTypeOrgService.getDebtPositionTypeOrgOperators(
        organizationId, debtPositionTypeOrgId, Pageable.ofSize(10), loggedUser,
        accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
    }
  }

  @Test
  void givenValidUserAndNullDebtPositionTypeOrgIdWhenGetDebtPositionTypeOrgOperatorsThenOk() {
    long organizationId = 1L;
    String organizationIpaCode = "ipaCode";

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.getOrganizations()
      .add(podamFactory.manufacturePojo(UserOrganizationRoles.class).toBuilder()
        .organizationId(organizationId)
        .organizationIpaCode(organizationIpaCode)
        .build());

    OperatorsPage operatorsPage = podamFactory.manufacturePojo(
      OperatorsPage.class);

    PagedDebtPositionTypeOrgOperatorDTO expectedResult = new PagedDebtPositionTypeOrgOperatorDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser)).thenAnswer(a -> null);

      Mockito.when(
          authzServiceMock.getOrganizationOperators(organizationIpaCode, null,
            null, null, 0, 10, accessToken))
        .thenReturn(operatorsPage);
      Mockito.when(
          debtPositionTypeOrgOperatorsMapperMock.mapToPagedDebtPositionTypeOrgOperatorDTO(
            operatorsPage, null))
        .thenReturn(expectedResult);

      PagedDebtPositionTypeOrgOperatorDTO result = debtPositionTypeOrgService.getDebtPositionTypeOrgOperators(
        organizationId, null, Pageable.ofSize(10), loggedUser, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
    }
  }


  @Test
  void givenInvalidUserWhenGetDebtPositionTypeOrgOperatorsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    Pageable pageable = Pageable.ofSize(10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionTypeOrgService.getDebtPositionTypeOrgOperators(organizationId, debtPositionTypeOrgId, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenAdminUserAndNoRelatedDebtPositionWhenDebtPositionTypeOrgThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");

    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;
    PagedModelDebtPosition debtPositions = new PagedModelDebtPosition();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Mockito.when(debtPositionServiceMock.getDebtPositionByDebtPositionTypeOrgId(Mockito.eq(debtPositionTypeOrgId),
        Mockito.argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 1), Mockito.eq(accessToken)))
      .thenReturn(debtPositions);

    Mockito.doNothing().when(debtPositionTypeOrgServiceMock).deleteDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);

    debtPositionTypeOrgService.deleteDebtPositionTypeOrg(organizationId, debtPositionTypeOrgId, loggedUser, accessToken);
  }

  @Test
  void givenRelatedDebtPositionWhenDebtPositionTypeOrgThenConflictException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");

    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;
    PagedModelDebtPosition debtPositions = new PagedModelDebtPosition();
    debtPositions.setEmbedded(new PagedModelDebtPositionEmbedded(List.of(new DebtPosition())));

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Mockito.when(debtPositionServiceMock.getDebtPositionByDebtPositionTypeOrgId(Mockito.eq(debtPositionTypeOrgId),
        Mockito.argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 1), Mockito.eq(accessToken)))
      .thenReturn(debtPositions);

    Assertions.assertThrows(ConflictException.class, () ->
      debtPositionTypeOrgService.deleteDebtPositionTypeOrg(organizationId, debtPositionTypeOrgId, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
  }

  @Test
  void givenValidDebtPositionTypeOrgAndAuthorizedUserWhenCreateDebtPositionTypeOrgThenOk() {
    long brokerId = 1L;
    Long organizationId = 2L;
    String organizationIpaCode = "organizationIpaCode";

    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(brokerId);
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionType debtPositionType = podamFactory.manufacturePojo(DebtPositionType.class);
    debtPositionType.setBrokerId(brokerId);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setDebtPositionTypeOrgId(null);
    debtPositionTypeOrg.setOrganizationId(organizationId);
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionType.getDebtPositionTypeId());
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);
    it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO generatedSaveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO.class);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(
        debtPositionType.getDebtPositionTypeId(), accessToken))
      .thenReturn(debtPositionType);
    Mockito.when(
      debtPositionTypeOrgMapperMock.mapToSaveDebtPositionTypeOrgDTO(
        saveDebtPositionTypeOrgDTO,
        loggedUser.getMappedExternalUserId(),
        organizationIpaCode,
        accessToken
      )).thenReturn(generatedSaveDebtPositionTypeOrgDTO);
    Mockito.when(debtPositionTypeOrgServiceMock.saveDebtPositionTypeOrg(
        generatedSaveDebtPositionTypeOrgDTO, accessToken))
      .thenReturn(debtPositionTypeOrg);

    DebtPositionTypeOrg result = debtPositionTypeOrgService.createDebtPositionTypeOrg(
      organizationId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken);

    Assertions.assertEquals(debtPositionTypeOrg, result);
  }

  @Test
  void givenInvalidDebtPositionTypeOrgWithWrongOrganizationIdWhenCreateDebtPositionTypeOrgThenOk() {
    long brokerId = 1L;
    Long organizationId = 2L;
    String organizationIpaCode = "organizationIpaCode";

    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(brokerId);
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setDebtPositionTypeOrgId(null);
    debtPositionTypeOrg.setOrganizationId(organizationId + 1);
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Assertions.assertThrows(InvalidDebtPositionTypeOrgException.class, () ->
      debtPositionTypeOrgService.createDebtPositionTypeOrg(
        organizationId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));

    Mockito.verifyNoInteractions(
      debtPositionTypeServiceMock, debtPositionTypeOrgMapperMock, debtPositionTypeOrgServiceMock);
  }

  @Test
  void givenInvalidDebtPositionTypeOrgWithPopulatedDebtPositionTypeOrgIdWhenCreateDebtPositionTypeOrgThenOk() {
    long brokerId = 1L;
    Long organizationId = 2L;
    String organizationIpaCode = "organizationIpaCode";

    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(brokerId);
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setDebtPositionTypeOrgId(3L);
    debtPositionTypeOrg.setOrganizationId(organizationId);
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Assertions.assertThrows(InvalidDebtPositionTypeOrgException.class, () ->
      debtPositionTypeOrgService.createDebtPositionTypeOrg(
        organizationId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));

    Mockito.verifyNoInteractions(
      debtPositionTypeServiceMock, debtPositionTypeOrgMapperMock, debtPositionTypeOrgServiceMock);
  }

  @Test
  void givenInvalidDebtPositionTypeOrgWithNonExistingDebtPositionTypeWhenCreateDebtPositionTypeOrgThenOk() {
    long brokerId = 1L;
    Long organizationId = 2L;
    String organizationIpaCode = "organizationIpaCode";

    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(brokerId);
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setDebtPositionTypeOrgId(null);
    debtPositionTypeOrg.setOrganizationId(organizationId);
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(
        debtPositionTypeOrg.getDebtPositionTypeId(), accessToken))
      .thenReturn(null);

    Assertions.assertThrows(InvalidDebtPositionTypeOrgException.class, () ->
      debtPositionTypeOrgService.createDebtPositionTypeOrg(
        organizationId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));
  }

  @Test
  void givenInvalidDebtPositionTypeOrgWithWrongBrokerIdWhenCreateDebtPositionTypeOrgThenOk() {
    long brokerId = 1L;
    Long organizationId = 2L;
    String organizationIpaCode = "organizationIpaCode";

    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(brokerId);
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionType debtPositionType = podamFactory.manufacturePojo(DebtPositionType.class);
    debtPositionType.setBrokerId(brokerId + 1);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setDebtPositionTypeOrgId(null);
    debtPositionTypeOrg.setOrganizationId(organizationId);
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionType.getDebtPositionTypeId());
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(
        debtPositionTypeOrg.getDebtPositionTypeId(), accessToken))
      .thenReturn(debtPositionType);

    Assertions.assertThrows(InvalidDebtPositionTypeOrgException.class, () ->
      debtPositionTypeOrgService.createDebtPositionTypeOrg(
        organizationId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));
  }

  @Test
  void givenValidDebtPositionTypeOrgAndUnauthorizedUserWhenCreateDebtPositionTypeOrgThenAuthorizationDeniedException() {
    long brokerId = 1L;
    Long organizationId = 2L;

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(brokerId);
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);

    Mockito.doThrow(new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId()))
      .when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      debtPositionTypeOrgService.createDebtPositionTypeOrg(
        organizationId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeServiceMock, debtPositionTypeOrgMapperMock, debtPositionTypeOrgServiceMock);
  }

  @Test
  void givenValidDebtPositionTypeOrgWhenUpdateDebtPositionTypeOrgThenOk() {
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;
    String organizationIpaCode = "organizationIpaCode";
    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);
    it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO generatedSaveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO.class);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(
        debtPositionTypeOrgId, accessToken))
      .thenReturn(buildUpdatedDebtPositionTypeOrg(debtPositionTypeOrg));
    Mockito.when(
      debtPositionTypeOrgMapperMock.mapToSaveDebtPositionTypeOrgDTO(
        saveDebtPositionTypeOrgDTO,
        loggedUser.getMappedExternalUserId(),
        organizationIpaCode,
        accessToken
      )).thenReturn(generatedSaveDebtPositionTypeOrgDTO);
    Mockito.when(debtPositionTypeOrgServiceMock.saveDebtPositionTypeOrg(
        generatedSaveDebtPositionTypeOrgDTO, accessToken))
      .thenReturn(debtPositionTypeOrg);

    DebtPositionTypeOrg result = debtPositionTypeOrgService.updateDebtPositionTypeOrg(
      organizationId, debtPositionTypeOrgId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken);

    Assertions.assertEquals(debtPositionTypeOrg, result);
  }

  @Test
  void givenInvalidDebtPositionTypeOrgWhenUpdateDebtPositionTypeOrgThenValidationException() {
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;
    String organizationIpaCode = "organizationIpaCode";
    UserOrganizationRoles organizationRoles = podamFactory.manufacturePojo(UserOrganizationRoles.class);
    organizationRoles.setOrganizationId(organizationId);
    organizationRoles.setOrganizationIpaCode(organizationIpaCode);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setOrganizations(Collections.singletonList(organizationRoles));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setDebtPositionTypeOrg(debtPositionTypeOrg);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(
        debtPositionTypeOrgId, accessToken))
      .thenReturn(podamFactory.manufacturePojo(DebtPositionTypeOrg.class));

    Assertions.assertThrows(ValidationException.class, () -> debtPositionTypeOrgService.updateDebtPositionTypeOrg(
      organizationId, debtPositionTypeOrgId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeOrgMapperMock);
  }

  private static DebtPositionTypeOrg buildUpdatedDebtPositionTypeOrg(DebtPositionTypeOrg debtPositionTypeOrg) {
    DebtPositionTypeOrg dpto = new DebtPositionTypeOrg();
    BeanUtils.copyProperties(debtPositionTypeOrg, dpto);
    return dpto.toBuilder()
      //updatable fields
      .iban(debtPositionTypeOrg.getIban() + 1)
      .postalIban(debtPositionTypeOrg.getPostalIban() + 1)
      .postalAccountCode(debtPositionTypeOrg.getPostalAccountCode() + 1)
      .holderPostalCc(debtPositionTypeOrg.getHolderPostalCc() + 1)
      .amountCents(debtPositionTypeOrg.getAmountCents() + 1)
      .externalPaymentUrl(debtPositionTypeOrg.getExternalPaymentUrl() + 1)
      .flagSpontaneous(!debtPositionTypeOrg.getFlagSpontaneous())
      .serviceId(debtPositionTypeOrg.getServiceId() + 1)
      .ioTemplateSubject(debtPositionTypeOrg.getIoTemplateSubject() + 1)
      .ioTemplateMessage(debtPositionTypeOrg.getIoTemplateMessage() + 1)
      .amountActualizationOrgSilServiceId(debtPositionTypeOrg.getAmountActualizationOrgSilServiceId() + 1)
      .notifyOutcomePushOrgSilServiceId(debtPositionTypeOrg.getNotifyOutcomePushOrgSilServiceId() + 1)
      .flagNotifyIo(!debtPositionTypeOrg.getFlagNotifyIo())
      .build();
  }

  @Test
  void givenNonExistingDebtPositionTypeOrgWhenUpdateDebtPositionTypeOrgThenResourceNotFoundException() {
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(
        debtPositionTypeOrgId, accessToken))
      .thenReturn(null);

    Assertions.assertThrows(ResourceNotFoundException.class, () -> debtPositionTypeOrgService.updateDebtPositionTypeOrg(
      organizationId, debtPositionTypeOrgId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeOrgMapperMock);
  }

  @Test
  void givenUnauthorizedUserWhenUpdateDebtPositionTypeOrgThenAuthorizationDeniedException() {
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);

    Mockito.doThrow(new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId()))
      .when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      debtPositionTypeOrgService.updateDebtPositionTypeOrg(
        organizationId, debtPositionTypeOrgId, saveDebtPositionTypeOrgDTO, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeOrgMapperMock, debtPositionTypeOrgServiceMock);
  }

  @Test
  void givenAuthorizedUserWhenValidateOperatorThenOk() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken))
                    .thenReturn(debtPositionTypeOrg);

    debtPositionTypeOrgService.validateOperator(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken);
  }

  @Test
  void givenUnauthorizedUserWhenValidateOperatorThenResourceNotFound() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken))
            .thenReturn(null);

    Assertions.assertThrows(ResourceNotFoundException.class, () ->
            debtPositionTypeOrgService.validateOperator(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken));
  }

  @Test
  void whenGetDebtPositionTypeOrgCodesThenOk(){
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";

    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);
    Set<String> expectedResult = debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId,accessToken))
            .thenReturn(debtPositionTypeOrgs);

    Set<String> result = debtPositionTypeOrgService.getDebtPositionTypeOrgCodes(organizationId, true, mappedExternalUserId,accessToken);

    assertNotNull(result);
    assertEquals(expectedResult,result);
  }

  @Test
  void givenNoDebtPositionTypeOrgsWhenGetDebtPositionTypeOrgCodesThenEmptySet(){
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId,accessToken))
            .thenReturn(null);

    Set<String> result = debtPositionTypeOrgService.getDebtPositionTypeOrgCodes(organizationId, true, mappedExternalUserId,accessToken);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void whenValidateIudsThenOk(){
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode(debtPositionTypeOrgCode);

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds,accessToken))
            .thenReturn(Collections.singletonList(debtPositionTypeOrg));

    debtPositionTypeOrgService.validateIuds(organizationId,debtPositionTypeOrgCode,iuds,accessToken);
  }

  @Test
  void givenNoMatchingDebtPositionTypeOrgCodeWhenValidateIudsThenIllegalArgumentException(){
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode(debtPositionTypeOrgCode+1);

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds,accessToken))
            .thenReturn(Collections.singletonList(debtPositionTypeOrg));

    assertThrows(IllegalArgumentException.class, () -> debtPositionTypeOrgService.validateIuds(organizationId,debtPositionTypeOrgCode,iuds,accessToken));
  }

  @Test
  void givenNoDebtPositionTypeOrgsWhenValidateIudsThenIllegalArgumentException(){
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds,accessToken))
            .thenReturn(Collections.emptyList());

    assertThrows(IllegalArgumentException.class, () -> debtPositionTypeOrgService.validateIuds(organizationId,debtPositionTypeOrgCode,iuds,accessToken));
  }

  @Test
  void givenMultipleDebtPositionTypeOrgsWhenValidateIudsThenIllegalArgumentException(){
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
      debtPositionTypeOrg.setCode(debtPositionTypeOrgCode);
      debtPositionTypeOrgs.add(debtPositionTypeOrg);
    }

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds,accessToken))
            .thenReturn(debtPositionTypeOrgs);

    assertThrows(IllegalArgumentException.class, () -> debtPositionTypeOrgService.validateIuds(organizationId,debtPositionTypeOrgCode,iuds,accessToken));
  }

  @Test
  void givenDebtPositionTypeOrgIdAndFlagActiveWhenUpdateFlagActiveDebtPositionTypeOrgThenOk() {
    //given
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");

    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 2L;

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.doNothing().when(debtPositionTypeOrgServiceMock).updateFlagActiveDebtPositionTypeOrg(debtPositionTypeOrgId, true, accessToken);
    //when
    Assertions.assertDoesNotThrow(() -> debtPositionTypeOrgService.updateFlagActiveDebtPositionTypeOrg(organizationId, debtPositionTypeOrgId, true, loggedUser, accessToken));
  }

  @Test
  void whenGetDebtPositionTypeOrgsThenOk() {
    String mappedExternalUserId = "mappedExternalUserId";

    long organizationId = 1L;
    CollectionModelDebtPositionTypeOrg collectionModel = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId, accessToken))
            .thenReturn(collectionModel);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId, accessToken);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertSame(collectionModel.getEmbedded().getDebtPositionTypeOrgs(), result);
  }

  @Test
  void givenNullEmbeddedWhenGetDebtPositionTypeOrgsThenEmptyList() {
    String mappedExternalUserId = "mappedExternalUserId";

    long organizationId = 1L;
    CollectionModelDebtPositionTypeOrg collectionModel = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);
    collectionModel.setEmbedded(null);

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId, accessToken))
            .thenReturn(collectionModel);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId, accessToken);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void givenNullCollectionModelWhenGetDebtPositionTypeOrgsThenEmptyList() {
    String mappedExternalUserId = "mappedExternalUserId";

    long organizationId = 1L;

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId, accessToken))
            .thenReturn(null);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, true, mappedExternalUserId, accessToken);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }


  @Test
  void whenGetDebtPositionTypeOrgByCodeThenOk() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";
    DebtPositionTypeOrg expectedResult = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken))
            .thenReturn(expectedResult);

    DebtPositionTypeOrg result = debtPositionTypeOrgService.getDebtPositionTypeOrgByCode(organizationId, debtPositionTypeOrgCode, mappedExternalUserId, accessToken);

    assertNotNull(result);
    assertEquals(expectedResult,result);
  }

  @Test
  void givenNoDebtPositionTypeOrgWhenGetDebtPositionTypeOrgByCodeThenResourceNotFound() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";

    Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken))
            .thenReturn(null);

    Assertions.assertThrows(ResourceNotFoundException.class, () ->
            debtPositionTypeOrgService.getDebtPositionTypeOrgByCode(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken));

  }
}
