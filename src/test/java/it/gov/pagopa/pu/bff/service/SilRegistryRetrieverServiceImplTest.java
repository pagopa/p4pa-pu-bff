package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.SilRegistryService;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedSilRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.SilRegistryMapper;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
import it.gov.pagopa.pu.registries.dto.generated.RegistryOutcome;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SilRegistryRetrieverServiceImplTest {
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private AuthorizationService authorizationServiceMock;

  @Mock
  private SilRegistryService silRegistryServiceMock;

  @Mock
  private SilRegistryMapper silRegistryMapperMock;

  @Mock
  private OrganizationRetrieverService organizationRetrieverServiceMock;

  private SilRegistryRetrieverService silRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    silRegistryRetrieverService = new SilRegistryRetrieverServiceImpl(
      authorizationServiceMock,
      silRegistryServiceMock,
      silRegistryMapperMock,
      organizationRetrieverServiceMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authorizationServiceMock,
      silRegistryServiceMock,
      silRegistryMapperMock,
      organizationRetrieverServiceMock
    );
  }

  @Test
  void givenMatchingOrgFiscalCodeWhenGetSilRegistryThenOk() {
    Long organizationId = 1L;
    String registryId = "registryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";
    SilRegistryDTO expectedResult = new SilRegistryDTO();
    expectedResult.setRegistryId(registryId);
    expectedResult.setOrgFiscalCode(orgFiscalCode);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(silRegistryServiceMock.getSilRegistry(registryId, accessToken)).thenReturn(expectedResult);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser, accessToken)).thenReturn(orgFiscalCode);

    SilRegistryDTO result = silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void givenNoSilRegistryWhenGetSilRegistryThenNull() {
    Long organizationId = 1L;
    String registryId = "registryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(silRegistryServiceMock.getSilRegistry(registryId, accessToken)).thenReturn(null);

    SilRegistryDTO result = silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken);

    assertNull(result);
    verifyNoInteractions(organizationRetrieverServiceMock);
  }

  @Test
  void givenNoMatchingOrgFiscalCodeWhenGetSilRegistryThenResourceNotFoundException() {
    Long organizationId = 1L;
    String registryId = "registryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";
    SilRegistryDTO expectedResult = new SilRegistryDTO();
    expectedResult.setRegistryId(registryId);
    expectedResult.setOrgFiscalCode(orgFiscalCode + "DIFF");

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(silRegistryServiceMock.getSilRegistry(registryId, accessToken)).thenReturn(expectedResult);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser, accessToken)).thenReturn(orgFiscalCode);

    assertThrows(ResourceNotFoundException.class, () ->
      silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken));
  }

  @Test
  void givenNoOrgFiscalCodeWhenGetSilRegistryThenResourceNotFoundException() {
    Long organizationId = 1L;
    String registryId = "registryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    SilRegistryDTO expectedResult = new SilRegistryDTO();
    expectedResult.setRegistryId(registryId);
    expectedResult.setOrgFiscalCode("someCode");

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(silRegistryServiceMock.getSilRegistry(registryId, accessToken)).thenReturn(expectedResult);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser, accessToken)).thenReturn(null);

    assertThrows(ResourceNotFoundException.class, () ->
      silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken));
  }

  @Test
  void givenPopulatedOrgFiscalCodeFilterWhenGetSilRegistriesThenOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";

    SilRegistryFiltersDTO filters = podamFactory.manufacturePojo(SilRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    PagedModelSilRegistry pagedModelSilRegistry = podamFactory.manufacturePojo(PagedModelSilRegistry.class);
    PagedSilRegistry expectedResult = podamFactory.manufacturePojo(PagedSilRegistry.class);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser, accessToken)).thenReturn(orgFiscalCode);
    when(silRegistryServiceMock.searchByFilters(orgFiscalCode, filters, pageable, accessToken)).thenReturn(pagedModelSilRegistry);
    when(silRegistryMapperMock.mapToPagedSilRegistry(pagedModelSilRegistry)).thenReturn(expectedResult);

    PagedSilRegistry result = silRegistryRetrieverService.getSilRegistries(organizationId, filters, pageable, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void givenOnlyOutcomeFilterWhenGetSilRegistriesThenOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";

    SilRegistryFiltersDTO filters = SilRegistryFiltersDTO.builder()
      .outcome(RegistryOutcome.OK)
      .eventDate(new OffsetDateTimeIntervalFilter())
      .build();

    Pageable pageable = Pageable.ofSize(10);
    PagedModelSilRegistry pagedModelSilRegistry = podamFactory.manufacturePojo(PagedModelSilRegistry.class);
    PagedSilRegistry expectedResult = podamFactory.manufacturePojo(PagedSilRegistry.class);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser, accessToken)).thenReturn(orgFiscalCode);
    when(silRegistryServiceMock.searchByFilters(orgFiscalCode, filters, pageable, accessToken)).thenReturn(pagedModelSilRegistry);
    when(silRegistryMapperMock.mapToPagedSilRegistry(pagedModelSilRegistry)).thenReturn(expectedResult);

    PagedSilRegistry result = silRegistryRetrieverService.getSilRegistries(
      organizationId, filters, pageable, loggedUser, accessToken
    );

    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void givenNoFilterWhenGetSilRegistriesThenIllegalArgumentException() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    SilRegistryFiltersDTO filters = SilRegistryFiltersDTO.builder()
      .eventDate(new OffsetDateTimeIntervalFilter())
      .build();
    Pageable pageable = Pageable.ofSize(10);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    assertThrows(IllegalArgumentException.class, () ->
      silRegistryRetrieverService.getSilRegistries(organizationId, filters, pageable, loggedUser, accessToken));

    verifyNoInteractions(organizationRetrieverServiceMock, silRegistryServiceMock, silRegistryMapperMock);
  }
}
