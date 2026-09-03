package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.OrganizationApi;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrganizationApi organizationApiMock;
  @Mock
  private HttpClientErrorException.BadRequest badRequestMock;

  private OrganizationClient organizationClient;

  @BeforeEach
  void setUp() {
    organizationClient = new OrganizationClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            organizationApisHolderMock,
            organizationApiMock,
            badRequestMock
    );
  }

  @Test
  void givenExistingOrganizationWhenUpdateOrganizationThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    OrganizationDetailDTO organizationDetailDTO = new OrganizationDetailDTO();

    when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    Mockito.doNothing().when(organizationApiMock).updateOrganization(organizationDetailDTO);

    organizationClient.updateOrganization(organizationDetailDTO, accessToken);

    Mockito.verifyNoMoreInteractions(organizationApisHolderMock,organizationApiMock);
  }

  @Test
  void givenNoExistentOrganizationWhenUpdateOrganizationThenResourceNotFoundException() {
    OrganizationDetailDTO organizationDetailDTO = new OrganizationDetailDTO();
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    doThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"))
            .when(organizationApiMock).updateOrganization(organizationDetailDTO);

    Assertions.assertThrows(NotFoundException.class,() -> organizationClient.updateOrganization(organizationDetailDTO, accessToken));
  }

}
