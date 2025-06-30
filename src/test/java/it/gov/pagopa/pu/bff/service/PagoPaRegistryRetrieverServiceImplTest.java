package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.PagoPaRegistryService;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagoPaRegistryMapper;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import it.gov.pagopa.pu.bff.service.pagopa_registry.PagoPaRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.pagopa_registry.PagoPaRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
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
class PagoPaRegistryRetrieverServiceImplTest {
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private OrganizationRetrieverService organizationRetrieverServiceMock;
  @Mock
  private PagoPaRegistryService pagoPaRegistryServiceMock;
  @Mock
  private PagoPaRegistryMapper pagoPaRegistryMapperMock;

  private PagoPaRegistryRetrieverService pagoPaRegistryRetrieverService;
  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    pagoPaRegistryRetrieverService = new PagoPaRegistryRetrieverServiceImpl(authorizationServiceMock, organizationRetrieverServiceMock,pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, organizationRetrieverServiceMock,
            pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @Test
  void givenPopulatedOrgFiscalCodeFilterWhenGetPagoPaRegistriesThenOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";

    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    PagedModelPagoPaRegistry pagedModelPagoPaRegistry = podamFactory.manufacturePojo(PagedModelPagoPaRegistry.class);
    PagedPagoPaRegistry expectedResult = podamFactory.manufacturePojo(PagedPagoPaRegistry.class);

      doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
      when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId,loggedUser,accessToken)).thenReturn(orgFiscalCode);
      when(pagoPaRegistryServiceMock.searchByFilters(orgFiscalCode,filters,pageable,accessToken)).thenReturn(pagedModelPagoPaRegistry);
      when(pagoPaRegistryMapperMock.mapToPagedPagoPaRegistry(pagedModelPagoPaRegistry)).thenReturn(expectedResult);

      PagedPagoPaRegistry result = pagoPaRegistryRetrieverService.getPagoPaRegistries(organizationId,filters,pageable,loggedUser,accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
  }

  @Test
  void givenNoFilterWhenGetPagoPaRegistriesThenIllegalArgumentException() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    PagoPaRegistryFiltersDTO filters = PagoPaRegistryFiltersDTO.builder()
            .eventDate(new OffsetDateTimeIntervalFilter())
            .build();
    Pageable pageable = Pageable.ofSize(10);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    assertThrows(IllegalArgumentException.class, ()->pagoPaRegistryRetrieverService.getPagoPaRegistries(organizationId,filters,pageable,loggedUser,accessToken));

    verifyNoInteractions(organizationRetrieverServiceMock,pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @Test
  void givenMatchingOrgFiscalCodeWhenGetPagoPaRegistryThenOk(){
    Long organizationId = 1L;
    String pagoPaRegistryId = "pagoPaRegistryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";
    PagoPaRegistryDTO expectedResult = podamFactory.manufacturePojo(PagoPaRegistryDTO.class);
    expectedResult.setOrgFiscalCode(orgFiscalCode);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(pagoPaRegistryServiceMock.getPagoPaRegistry(pagoPaRegistryId,accessToken))
            .thenReturn(expectedResult);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser,accessToken))
            .thenReturn(orgFiscalCode);

    PagoPaRegistryDTO result = pagoPaRegistryRetrieverService.getPagoPaRegistry(organizationId,pagoPaRegistryId,loggedUser,accessToken);

    assertNotNull(result);
    assertSame(expectedResult,result);
  }

  @Test
  void givenNoMatchingOrgFiscalCodeWhenGetPagoPaRegistryThenResourceNotFoundException(){
    Long organizationId = 1L;
    String pagoPaRegistryId = "pagoPaRegistryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    String orgFiscalCode = "orgFiscalCode";
    PagoPaRegistryDTO expectedResult = podamFactory.manufacturePojo(PagoPaRegistryDTO.class);
    expectedResult.setOrgFiscalCode(orgFiscalCode+1);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(pagoPaRegistryServiceMock.getPagoPaRegistry(pagoPaRegistryId,accessToken))
            .thenReturn(expectedResult);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser,accessToken))
            .thenReturn(orgFiscalCode);

    assertThrows(ResourceNotFoundException.class,()->pagoPaRegistryRetrieverService.getPagoPaRegistry(organizationId,pagoPaRegistryId,loggedUser,accessToken));
  }

  @Test
  void givenNoOrgFiscalCodeWhenGetPagoPaRegistryThenResourceNotFoundException(){
    Long organizationId = 1L;
    String pagoPaRegistryId = "pagoPaRegistryId";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);
    PagoPaRegistryDTO expectedResult = podamFactory.manufacturePojo(PagoPaRegistryDTO.class);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(pagoPaRegistryServiceMock.getPagoPaRegistry(pagoPaRegistryId,accessToken))
            .thenReturn(expectedResult);
    when(organizationRetrieverServiceMock.getOrgFiscalCode(organizationId, loggedUser,accessToken))
            .thenReturn(null);

    assertThrows(ResourceNotFoundException.class,()->pagoPaRegistryRetrieverService.getPagoPaRegistry(organizationId,pagoPaRegistryId,loggedUser,accessToken));
  }
}

