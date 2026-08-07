package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.organization.controller.generated.OrgSilServiceEntityControllerApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrgSilServiceEntityClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrgSilServiceEntityControllerApi orgSilServiceEntityControllerApiMock;
  private OrgSilServiceEntityClient orgSilServiceEntityClient;

  @BeforeEach
  void setUp() {
    orgSilServiceEntityClient = new OrgSilServiceEntityClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(organizationApisHolderMock, orgSilServiceEntityControllerApiMock);
  }


  @Test
  void whenDeleteOrgSilServiceThenInvokeWithAccessToken() {
    Long orgSilServiceId = 123L;
    String accessToken = "ACCESS_TOKEN";

    when(organizationApisHolderMock.getOrgSilServiceEntityControllerApi(accessToken))
      .thenReturn(orgSilServiceEntityControllerApiMock);

    orgSilServiceEntityClient.deleteOrgSilService(orgSilServiceId, accessToken);

    verify(orgSilServiceEntityControllerApiMock, times(1))
      .crudDeleteOrgsilservice(String.valueOf(orgSilServiceId));
  }

  @Test
  void givenNonExistentOrgSilServiceIdWhenDeleteThenThrowResourceNotFoundException() {
    Long orgSilServiceId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolderMock.getOrgSilServiceEntityControllerApi(accessToken))
      .thenReturn(orgSilServiceEntityControllerApiMock);
    doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null))
      .when(orgSilServiceEntityControllerApiMock)
      .crudDeleteOrgsilservice(String.valueOf(orgSilServiceId));

    Assertions.assertThrows(NotFoundException.class, () ->
      orgSilServiceEntityClient.deleteOrgSilService(orgSilServiceId, accessToken));
  }
}
