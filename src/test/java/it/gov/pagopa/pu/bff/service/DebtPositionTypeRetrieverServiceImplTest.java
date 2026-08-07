package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.exception.common.ConflictException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.taxonomy.TaxonomyRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeService debtPositionTypeServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapperMock;
  @Mock
  private TaxonomyRetrieverService taxonomyRetrieverServiceMock;
  @Mock
  private DebtPositionTypeMapper debtPositionTypeMapperMock;
  @Mock
  private OrganizationService organizationServiceMock;

  private DebtPositionTypeRetrieverServiceImpl debtPositionTypeRetrieverService;

  private DebtPositionType debtPositionType;

  private final String accessToken = "TOKEN";

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();


  @BeforeEach
  void setUp() {
    debtPositionType = new DebtPositionType();
    debtPositionType.setDebtPositionTypeId(123L);
    debtPositionType.setBrokerId(456L);
    debtPositionType.setCode("CODE001");
    debtPositionType.setDescription("Test Description");
    debtPositionType.setOrgType("OrgType001");
    debtPositionType.setMacroArea("MacroArea001");
    debtPositionType.setServiceType("ServiceType001");
    debtPositionType.setCollectingReason("Collecting Reason 001");
    debtPositionType.setTaxonomyCode("TaxonomyCode001");
    debtPositionType.setFlagAnonymousFiscalCode(true);
    debtPositionType.setFlagMandatoryDueDate(false);
    debtPositionType.setFlagNotifyIo(true);
    debtPositionType.setIoTemplateMessage("Test IO Template Message");

    debtPositionTypeRetrieverService = new DebtPositionTypeRetrieverServiceImpl(debtPositionTypeServiceMock, debtPositionTypeWithCountMapperMock, taxonomyRetrieverServiceMock, authorizationServiceMock, debtPositionTypeMapperMock, debtPositionTypeOrgServiceMock, organizationServiceMock);
  }

  @Test
  void testGetDebtPositionTypeById() {
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(debtPositionType);

    DebtPositionType result = debtPositionTypeRetrieverService.getDebtPositionTypeById(accessToken, 123L);

    assertNotNull(result);
    assertEquals(123L, result.getDebtPositionTypeId());
    assertEquals(456L, result.getBrokerId());
    assertEquals("CODE001", result.getCode());
    assertEquals("Test Description", result.getDescription());
    assertEquals("OrgType001", result.getOrgType());
    assertEquals("MacroArea001", result.getMacroArea());
    assertEquals("ServiceType001", result.getServiceType());
    assertEquals("Collecting Reason 001", result.getCollectingReason());
    assertEquals("TaxonomyCode001", result.getTaxonomyCode());
    assertEquals(Boolean.TRUE, result.getFlagAnonymousFiscalCode());
    assertEquals(Boolean.FALSE, result.getFlagMandatoryDueDate());
    assertEquals(Boolean.TRUE, result.getFlagNotifyIo());
    assertEquals("Test IO Template Message", result.getIoTemplateMessage());
  }

  @Test
  void testGetDebtPositionTypeById_NullResponse() {
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(null);

    DebtPositionType result = debtPositionTypeRetrieverService.getDebtPositionTypeById(accessToken, 123L);

    assertNull(result);
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeWithCountThenOK() {
    long brokerId = 1L;
    String code = "code";
    String description = "description";
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PagedDebtPositionTypeWithCount pagedDebtPositionTypeWithCount = new PagedDebtPositionTypeWithCount();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(1L, userInfo);
    when(debtPositionTypeServiceMock.getDebtPositionTypeWithCount(brokerId, code, description, PageRequest.of(0, 10), accessToken)).thenReturn(pagedModelDebtPositionTypeWithCount);
    when(debtPositionTypeWithCountMapperMock.mapToPagedDebtPositionWithCount(pagedModelDebtPositionTypeWithCount)).thenReturn(pagedDebtPositionTypeWithCount);

    PagedDebtPositionTypeWithCount result = debtPositionTypeRetrieverService.getDebtPositionTypeWithCount(
      1L, code, description, PageRequest.of(0, 10),
      userInfo, accessToken);

    assertNotNull(result);
    assertSame(pagedDebtPositionTypeWithCount, result);

    Mockito.verifyNoMoreInteractions(debtPositionTypeServiceMock, debtPositionTypeWithCountMapperMock, authorizationServiceMock);
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeWithCountThenAuthorizationDeniedException() {
    long brokerId = 1L;
    String code = "code";
    String description = "description";
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PageRequest pageRequest = PageRequest.of(0, 10);

    doThrow(new AuthorizationDeniedException("")).when(authorizationServiceMock).validateAdminRole(1L, userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      debtPositionTypeRetrieverService.getDebtPositionTypeWithCount(
        1L, code, description, pageRequest, userInfo, accessToken));

    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionTypeServiceMock, debtPositionTypeWithCountMapperMock);
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeDetailThenOk() {
    long brokerId = 456L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    Taxonomy taxonomy = podamFactory.manufacturePojo(Taxonomy.class);

    DebtPositionTypeDetailDTO expectedResult = new DebtPositionTypeDetailDTO();

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), anyString()))
      .thenReturn(debtPositionType);
    when(taxonomyRetrieverServiceMock.getTaxonomyByTaxonomyCode(Mockito.eq(debtPositionType.getTaxonomyCode()), anyString()))
      .thenReturn(taxonomy);
    when(debtPositionTypeMapperMock.mapToDebtPositionTypeDetailDTO(debtPositionType, taxonomy))
      .thenReturn(expectedResult);

    DebtPositionTypeDetailDTO result = debtPositionTypeRetrieverService.getDebtPositionTypeDetail(1L, debtPositionType.getDebtPositionTypeId(), userInfo, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void givenNotValidUserWhenGetDebtPositionTypeDetailThenAuthorizationDeniedException() {
    UserInfo userInfo = new UserInfo();
    Long debtPositionTypeId = debtPositionType.getDebtPositionTypeId();

    doThrow(new AuthorizationDeniedException("AuthorizationDeniedException")).when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class, () -> debtPositionTypeRetrieverService.getDebtPositionTypeDetail(1L, debtPositionTypeId, userInfo, accessToken));

    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(taxonomyRetrieverServiceMock, debtPositionTypeMapperMock);
  }


  @Test
  void givenNotMatchingBrokerIdWhenGetDebtPositionTypeDetailThenReturnNull() {
    long brokerId = 1L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), anyString()))
      .thenReturn(debtPositionType);

    DebtPositionTypeDetailDTO result = debtPositionTypeRetrieverService.getDebtPositionTypeDetail(1L, debtPositionType.getDebtPositionTypeId(), userInfo, accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(taxonomyRetrieverServiceMock, debtPositionTypeMapperMock);
  }


  @Test
  void givenEmptyTaxonomyWhenGetDebtPositionTypeDetailThenReturnNull() {
    long brokerId = 456L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), anyString()))
      .thenReturn(debtPositionType);
    when(taxonomyRetrieverServiceMock.getTaxonomyByTaxonomyCode(Mockito.eq(debtPositionType.getTaxonomyCode()), anyString()))
      .thenReturn(null);

    DebtPositionTypeDetailDTO result = debtPositionTypeRetrieverService.getDebtPositionTypeDetail(1L, debtPositionType.getDebtPositionTypeId(), userInfo, accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(debtPositionTypeMapperMock);
  }

  @Test
  void givenValidUserWhenCreateDebtPositionTypeThenOk() {
    long brokerId = 456L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    DebtPositionTypeRequestBody debtPositionTypeRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypeRequestBody.class);
    debtPositionTypeRequestBody.setDebtPositionTypeId(null);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    when(debtPositionTypeServiceMock.createDebtPositionType(eq(debtPositionTypeRequestBody), anyString()))
      .thenReturn(debtPositionType);

    DebtPositionType result = debtPositionTypeRetrieverService.createDebtPositionType(debtPositionTypeRequestBody, userInfo, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(debtPositionType, result);
  }

  @Test
  void givenPopulatedIdWhenCreateDebtPositionTypeThenIllegalArgumentException() {
    long brokerId = 456L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    DebtPositionTypeRequestBody debtPositionTypeRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypeRequestBody.class);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

    Assertions.assertThrows(IllegalArgumentException.class, () -> debtPositionTypeRetrieverService.createDebtPositionType(debtPositionTypeRequestBody, userInfo, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeServiceMock);
  }

  @Test
  void givenNotValidUserWhenCreateDebtPositionTypeThenAuthorizationDeniedException() {
    UserInfo userInfo = new UserInfo();
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypeRequestBody.class);

    doThrow(new AuthorizationDeniedException("AuthorizationDeniedException")).when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class, () -> debtPositionTypeRetrieverService.createDebtPositionType(debtPositionTypeRequestBody, userInfo, accessToken));

    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionTypeServiceMock);
  }

  @Test
  void givenValidUserWhenPatchDebtPositionTypeThenOk() {
    long brokerId = 456L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    Long debtPositionTypeId = 1L;
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypePatchRequestBody.class);
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypeRequestBody.class);
    DebtPositionType oldDpType = podamFactory.manufacturePojo(DebtPositionType.class);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(debtPositionTypeId, accessToken)).thenReturn(oldDpType);
    when(debtPositionTypeMapperMock.mapToDebtPositionTypeRequestBody(debtPositionTypePatchRequestBody, oldDpType)).thenReturn(debtPositionTypeRequestBody);
    when(debtPositionTypeServiceMock.patchDebtPositionType(eq(debtPositionTypeId), eq(debtPositionTypeRequestBody), anyString()))
      .thenReturn(debtPositionType);

    DebtPositionType result = debtPositionTypeRetrieverService.patchDebtPositionType(debtPositionTypeId, debtPositionTypePatchRequestBody, userInfo, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(debtPositionType, result);
  }

  @Test
  void givenNotValidUserWhenPatchDebtPositionTypeThenAuthorizationDeniedException() {
    UserInfo userInfo = new UserInfo();
    Long debtPositionTypeId = 1L;
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypePatchRequestBody.class);

    doThrow(new AuthorizationDeniedException("AuthorizationDeniedException")).when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class, () -> debtPositionTypeRetrieverService.patchDebtPositionType(debtPositionTypeId, debtPositionTypePatchRequestBody, userInfo, accessToken));

    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionTypeMapperMock, debtPositionTypeServiceMock);
  }

  @Test
  void givenNonExistentDebtPositionTypeWhenPatchDebtPositionTypeThenReturnNull() {
    long brokerId = 1L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);

    Long debtPositionTypeId = 1L;
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypePatchRequestBody.class);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(debtPositionTypeId, accessToken)).thenReturn(null);

    DebtPositionType result = debtPositionTypeRetrieverService.patchDebtPositionType(debtPositionTypeId, debtPositionTypePatchRequestBody, userInfo, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenValidRequestWhenDeleteDebtPositionTypeThenOk() {
    long debtPositionTypeId = 123L;
    UserInfo loggedUser = new UserInfo();

    PagedModelDebtPositionTypeOrg pagedModel = new PagedModelDebtPositionTypeOrg();

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId, PageRequest.of(0, 1), accessToken))
      .thenReturn(pagedModel);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.doNothing().when(debtPositionTypeServiceMock).deleteDebtPositionType(debtPositionTypeId, accessToken);

    debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, loggedUser, accessToken);

    Mockito.verifyNoMoreInteractions(authorizationServiceMock, debtPositionTypeOrgServiceMock, debtPositionTypeServiceMock);
  }

  @Test
  void givenDebtPositionTypeAssociatedWithOrgsWhenDeleteDebtPositionTypeThenConflictException() {
    long debtPositionTypeId = 123L;
    UserInfo loggedUser = new UserInfo();

    PagedModelDebtPositionTypeOrg pagedModel = new PagedModelDebtPositionTypeOrg();
    PagedModelDebtPositionTypeOrgEmbedded embedded = new PagedModelDebtPositionTypeOrgEmbedded();
    embedded.setDebtPositionTypeOrgs(List.of(new DebtPositionTypeOrg()));
    pagedModel.setEmbedded(embedded);

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId, PageRequest.of(0, 1), accessToken))
      .thenReturn(pagedModel);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    Assertions.assertThrows(ConflictException.class, () ->
      debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, loggedUser, accessToken));

    verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    verify(debtPositionTypeOrgServiceMock).getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId, PageRequest.of(0, 1), accessToken);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, debtPositionTypeOrgServiceMock);
    Mockito.verifyNoInteractions(debtPositionTypeServiceMock);
  }

  @Test
  void givenInvalidUserWhenDeleteDebtPositionTypeThenAuthorizationDeniedException() {
    long debtPositionTypeId = 123L;
    UserInfo loggedUser = new UserInfo();

    doThrow(new AuthorizationDeniedException("")).when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, loggedUser, accessToken));

    verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock, debtPositionTypeServiceMock);
  }

  @Test
  void whenGetDebtPositionTypesByOrganizationIdThenReturnFilteredDebtPositionTypeList() {
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    Organization organization = new Organization();
    organization.setBrokerId(456L);
    organization.setOrgTypeCode("OrgType001");

    CollectionModelDebtPositionType collectionModel = getCollectionModelDebtPositionType();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(invocation -> null);

      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken)).thenReturn(organization);
      when(debtPositionTypeServiceMock.getDebtPositionTypesByBrokerIdAndOrgType(456L, "OrgType001", accessToken)).thenReturn(collectionModel);

      List<DebtPositionType> result = debtPositionTypeRetrieverService.getDebtPositionTypesByOrganizationId(organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals("VALID", result.getFirst().getCode());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      verify(organizationServiceMock).getOrganizationByOrganizationId(organizationId, accessToken);
      verify(debtPositionTypeServiceMock).getDebtPositionTypesByBrokerIdAndOrgType(456L, "OrgType001", accessToken);
    }
  }

  private static CollectionModelDebtPositionType getCollectionModelDebtPositionType() {
    DebtPositionType validType = new DebtPositionType();
    validType.setDebtPositionTypeId(1L);
    validType.setCode("VALID");

    DebtPositionType unknownType = new DebtPositionType();
    unknownType.setDebtPositionTypeId(2L);
    unknownType.setCode(DebtPositionTypeRetrieverServiceImpl.DEBT_POSITION_TYPE_CODE_UNKNOWN);

    List<DebtPositionType> inputList = List.of(validType, unknownType);

    PagedModelDebtPositionTypeEmbedded embedded = new PagedModelDebtPositionTypeEmbedded();
    embedded.setDebtPositionTypes(inputList);

    CollectionModelDebtPositionType collectionModel = new CollectionModelDebtPositionType();
    collectionModel.setEmbedded(embedded);
    return collectionModel;
  }

  @Test
  void whenDebtPositionTypesIsEmptyThenReturnEmptyList() {
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    Organization organization = new Organization();
    organization.setBrokerId(456L);
    organization.setOrgTypeCode("OrgType001");

    PagedModelDebtPositionTypeEmbedded embedded = new PagedModelDebtPositionTypeEmbedded();
    embedded.setDebtPositionTypes(Collections.emptyList());

    CollectionModelDebtPositionType collectionModel = new CollectionModelDebtPositionType();
    collectionModel.setEmbedded(embedded);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(invocation -> null);

      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken)).thenReturn(organization);
      when(debtPositionTypeServiceMock.getDebtPositionTypesByBrokerIdAndOrgType(456L, "OrgType001", accessToken)).thenReturn(collectionModel);

      List<DebtPositionType> result = debtPositionTypeRetrieverService.getDebtPositionTypesByOrganizationId(organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(result.isEmpty());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      verify(organizationServiceMock).getOrganizationByOrganizationId(organizationId, accessToken);
      verify(debtPositionTypeServiceMock).getDebtPositionTypesByBrokerIdAndOrgType(456L, "OrgType001", accessToken);
    }
  }

}

