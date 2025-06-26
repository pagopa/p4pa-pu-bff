package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.connector.registries.PagoPaRegistryService;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagoPaRegistryMapper;
import it.gov.pagopa.pu.bff.service.pagopa_registry.PagoPaRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.pagopa_registry.PagoPaRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
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
  private OrganizationService organizationServiceMock;
  @Mock
  private PagoPaRegistryService pagoPaRegistryServiceMock;
  @Mock
  private PagoPaRegistryMapper pagoPaRegistryMapperMock;

  private PagoPaRegistryRetrieverService pagoPaRegistryRetrieverService;
  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    pagoPaRegistryRetrieverService = new PagoPaRegistryRetrieverServiceImpl(authorizationServiceMock,organizationServiceMock,pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authorizationServiceMock,organizationServiceMock,
            pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @Test
  void givenPopulatedOrgFiscalCodeFilterWhenGetPagoPaRegistriesThenOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    PagedModelPagoPaRegistry pagedModelPagoPaRegistry = podamFactory.manufacturePojo(PagedModelPagoPaRegistry.class);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(loggedUser.getBrokerId());
    organization.setOrganizationId(organizationId);
    PagedPagoPaRegistry expectedResult = podamFactory.manufacturePojo(PagedPagoPaRegistry.class);

      doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);
      when(pagoPaRegistryServiceMock.searchByFilters(organization.getOrgFiscalCode(),filters,pageable,accessToken)).thenReturn(pagedModelPagoPaRegistry);
      when(pagoPaRegistryMapperMock.mapToPagedPagoPaRegistry(pagedModelPagoPaRegistry)).thenReturn(expectedResult);

      PagedPagoPaRegistry result = pagoPaRegistryRetrieverService.getPagoPaRegistries(organizationId,filters,pageable,loggedUser,accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
  }

  @Test
  void givenInvalidBrokerWhenGetPagoPaRegistriesThenResourceNotFoundException() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(3L);
    organization.setOrganizationId(organizationId);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    assertThrows(ResourceNotFoundException.class, ()->pagoPaRegistryRetrieverService.getPagoPaRegistries(organizationId,filters,pageable,loggedUser,accessToken));

    verifyNoInteractions(pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @Test
  void givenNoOrganizationBrokerWhenGetPagoPaRegistriesThenResourceNotFoundException() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(null);
    organization.setOrganizationId(organizationId);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    assertThrows(ResourceNotFoundException.class, ()->pagoPaRegistryRetrieverService.getPagoPaRegistries(organizationId,filters,pageable,loggedUser,accessToken));

    verifyNoInteractions(pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }

  @Test
  void givenNoOrganizationWhenGetPagoPaRegistriesThenResourceNotFoundException() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    loggedUser.setBrokerId(2L);

    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);

    doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(null);

    assertThrows(ResourceNotFoundException.class, ()->pagoPaRegistryRetrieverService.getPagoPaRegistries(organizationId,filters,pageable,loggedUser,accessToken));

    verifyNoInteractions(pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
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

    verifyNoInteractions(organizationServiceMock,pagoPaRegistryServiceMock,pagoPaRegistryMapperMock);
  }
}

