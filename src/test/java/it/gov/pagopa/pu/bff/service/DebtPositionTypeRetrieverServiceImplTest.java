package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.taxonomy.TaxonomyRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeService debtPositionTypeServiceMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapperMock;
  @Mock
  private TaxonomyRetrieverService taxonomyRetrieverServiceMock;
  @Mock
  private DebtPositionTypeMapper debtPositionTypeMapperMock;
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;

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

    debtPositionTypeRetrieverService = new DebtPositionTypeRetrieverServiceImpl(debtPositionTypeServiceMock, debtPositionTypeWithCountMapperMock, taxonomyRetrieverServiceMock, authorizationServiceMock, debtPositionTypeMapperMock, debtPositionApisHolderMock);
  }

  @Test
  void testGetDebtPositionTypeById() {
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), any()))
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
    assertTrue(result.getFlagAnonymousFiscalCode());
    assertFalse(result.getFlagMandatoryDueDate());
    assertTrue(result.getFlagNotifyIo());
    assertEquals("Test IO Template Message", result.getIoTemplateMessage());
  }

  @Test
  void testGetDebtPositionTypeById_NullResponse() {
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(null);

    DebtPositionType result = debtPositionTypeRetrieverService.getDebtPositionTypeById(accessToken, 123L);

    assertNull(result);
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeWithCountThenOK() {
    long brokerId = 1L;
    String description = "description";
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PagedDebtPositionTypeWithCount pagedDebtPositionTypeWithCount = new PagedDebtPositionTypeWithCount();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(1L, userInfo);
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeWithCount(brokerId, description, PageRequest.of(0, 10), accessToken)).thenReturn(pagedModelDebtPositionTypeWithCount);
    Mockito.when(debtPositionTypeWithCountMapperMock.mapToPagedDebtPositionWithCount(pagedModelDebtPositionTypeWithCount)).thenReturn(pagedDebtPositionTypeWithCount);

    PagedDebtPositionTypeWithCount result = debtPositionTypeRetrieverService.getDebtPositionTypeWithCount(
      1L, description, PageRequest.of(0, 10),
      userInfo, accessToken);

    assertNotNull(result);
    assertSame(pagedDebtPositionTypeWithCount, result);

    Mockito.verifyNoMoreInteractions(debtPositionTypeServiceMock, debtPositionTypeWithCountMapperMock, authorizationServiceMock);
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeWithCountThenAuthorizationDeniedException() {
    long brokerId = 1L;
    String description = "description";
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PageRequest pageRequest = PageRequest.of(0, 10);

    Mockito.doThrow(new AuthorizationDeniedException("")).when(authorizationServiceMock).validateAdminRole(1L, userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      debtPositionTypeRetrieverService.getDebtPositionTypeWithCount(
        1L, description, pageRequest, userInfo, accessToken));

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
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), anyString()))
      .thenReturn(debtPositionType);
    Mockito.when(taxonomyRetrieverServiceMock.getTaxonomyByTaxonomyCode(Mockito.eq(debtPositionType.getTaxonomyCode()), anyString()))
      .thenReturn(taxonomy);
    Mockito.when(debtPositionTypeMapperMock.mapToDebtPositionTypeDetailDTO(debtPositionType, taxonomy))
      .thenReturn(expectedResult);

    DebtPositionTypeDetailDTO result = debtPositionTypeRetrieverService.getDebtPositionTypeDetail(1L, debtPositionType.getDebtPositionTypeId(), userInfo, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void givenNotValidUserWhenGetDebtPositionTypeDetailThenAuthorizationDeniedException() {
    UserInfo userInfo = new UserInfo();
    Long debtPositionTypeId = debtPositionType.getDebtPositionTypeId();

    Mockito.doThrow(new AuthorizationDeniedException("AuthorizationDeniedException")).when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

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
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), anyString()))
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
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), anyString()))
      .thenReturn(debtPositionType);
    Mockito.when(taxonomyRetrieverServiceMock.getTaxonomyByTaxonomyCode(Mockito.eq(debtPositionType.getTaxonomyCode()), anyString()))
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
    Mockito.when(debtPositionTypeServiceMock.createDebtPositionType(eq(debtPositionTypeRequestBody), anyString()))
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

    Mockito.doThrow(new AuthorizationDeniedException("AuthorizationDeniedException")).when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

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

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    Mockito.when(debtPositionTypeMapperMock.mapToDebtPositionTypeRequestBody(debtPositionTypePatchRequestBody)).thenReturn(debtPositionTypeRequestBody);
    Mockito.when(debtPositionTypeServiceMock.patchDebtPositionType(eq(debtPositionTypeId), eq(debtPositionTypeRequestBody), anyString()))
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

    Mockito.doThrow(new AuthorizationDeniedException("AuthorizationDeniedException")).when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

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
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = podamFactory.manufacturePojo(
      DebtPositionTypeRequestBody.class);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    Mockito.when(debtPositionTypeMapperMock.mapToDebtPositionTypeRequestBody(debtPositionTypePatchRequestBody)).thenReturn(debtPositionTypeRequestBody);
    Mockito.when(debtPositionTypeServiceMock.patchDebtPositionType(eq(debtPositionTypeId), eq(debtPositionTypeRequestBody), anyString()))
      .thenReturn(null);

    DebtPositionType result = debtPositionTypeRetrieverService.patchDebtPositionType(debtPositionTypeId, debtPositionTypePatchRequestBody, userInfo, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenValidRequestWhenDeleteDebtPositionTypeThenNoContent() {
    long debtPositionTypeId = 123L;
    UserInfo loggedUser = new UserInfo();

    DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock = Mockito.mock(DebtPositionTypeOrgSearchControllerApi.class);
    Mockito.when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);

    Mockito.when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByDebtPositionTypeId(debtPositionTypeId))
      .thenReturn(new CollectionModelDebtPositionTypeOrg());

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.doNothing().when(debtPositionTypeServiceMock).deleteDebtPositionType(debtPositionTypeId, accessToken);

    debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, loggedUser, accessToken);

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(debtPositionApisHolderMock).getDebtPositionTypeOrgSearchControllerApi(accessToken);
    Mockito.verify(debtPositionTypeOrgSearchControllerApiMock).crudDebtPositionTypeOrgsFindByDebtPositionTypeId(debtPositionTypeId);
    Mockito.verify(debtPositionTypeServiceMock).deleteDebtPositionType(debtPositionTypeId, accessToken);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, debtPositionApisHolderMock, debtPositionTypeServiceMock);
  }

  @Test
  void givenDebtPositionTypeAssociatedWithOrgsWhenDeleteDebtPositionTypeThenConflictException() {
    long debtPositionTypeId = 123L;
    UserInfo loggedUser = new UserInfo();

    DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock = Mockito.mock(DebtPositionTypeOrgSearchControllerApi.class);
    Mockito.when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);

    CollectionModelDebtPositionTypeOrg collectionModel = new CollectionModelDebtPositionTypeOrg();
    PagedModelDebtPositionTypeOrgEmbedded embedded = new PagedModelDebtPositionTypeOrgEmbedded();
    embedded.setDebtPositionTypeOrgs(List.of(new DebtPositionTypeOrg()));
    collectionModel.setEmbedded(embedded);

    Mockito.when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByDebtPositionTypeId(debtPositionTypeId))
      .thenReturn(collectionModel);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    Assertions.assertThrows(ConflictException.class, () ->
      debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(debtPositionApisHolderMock).getDebtPositionTypeOrgSearchControllerApi(accessToken);
    Mockito.verify(debtPositionTypeOrgSearchControllerApiMock).crudDebtPositionTypeOrgsFindByDebtPositionTypeId(debtPositionTypeId);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, debtPositionApisHolderMock);
    Mockito.verifyNoInteractions(debtPositionTypeServiceMock);
  }

  @Test
  void givenInvalidUserWhenDeleteDebtPositionTypeThenAuthorizationDeniedException() {
    long debtPositionTypeId = 123L;
    UserInfo loggedUser = new UserInfo();

    Mockito.doThrow(new AuthorizationDeniedException("")).when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      debtPositionTypeRetrieverService.deleteDebtPositionType(debtPositionTypeId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionApisHolderMock, debtPositionTypeServiceMock);
  }

}

