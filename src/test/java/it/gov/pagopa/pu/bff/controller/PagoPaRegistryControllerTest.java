package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.pagopa_registry.PagoPaRegistryRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PagoPaRegistryControllerTest {
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private PagoPaRegistryRetrieverService pagoPaRegistryRetrieverService;
  @InjectMocks
  private PagoPaRegistryController pagoPaRegistryController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            pagoPaRegistryRetrieverService
    );
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetPagoPaRegistriesThenOk() {
    long organizationId = 1L;
    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    PagedPagoPaRegistry expectedResult = podamFactory.manufacturePojo(PagedPagoPaRegistry.class);

    Mockito.when(pagoPaRegistryRetrieverService.getPagoPaRegistries(
                    organizationId, filters, Pageable.ofSize(10), loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedPagoPaRegistry> response = pagoPaRegistryController.getPagoPaRegistries(
            organizationId,
            filters.getEventType(),
            filters.getEventDate().getFrom(),
            filters.getEventDate().getTo(),
            filters.getIuv(),
            Pageable.ofSize(10));

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetPagoPaRegistryThenOk() {
    long organizationId = 1L;
    String pagoPaRegistryId = "pagoPaRegistryId";
    PagoPaRegistryDTO expectedResult = podamFactory.manufacturePojo(PagoPaRegistryDTO.class);

    Mockito.when(pagoPaRegistryRetrieverService.getPagoPaRegistry(
                    organizationId, pagoPaRegistryId, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagoPaRegistryDTO> response = pagoPaRegistryController.getPagoPaRegistry(
            organizationId, pagoPaRegistryId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}

