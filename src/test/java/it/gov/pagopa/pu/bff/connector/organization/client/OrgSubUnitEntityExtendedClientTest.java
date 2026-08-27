package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrgSubUnitEntityExtendedControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitEntityExtendedClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrgSubUnitEntityExtendedControllerApi orgSubUnitEntityExtendedControllerApiControllerApiMock;

  private OrgSubUnitEntityExtendedClient orgSubUnitEntityExtendedClient;

  @BeforeEach
  void setUp() {
    orgSubUnitEntityExtendedClient = new OrgSubUnitEntityExtendedClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      orgSubUnitEntityExtendedControllerApiControllerApiMock)
    ;
  }

  @Test
  void whenThenInvokeWithAccessToken() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus orgSubUnitStatus = OrgSubUnitStatus.CANCELLED;
    String accessToken = "accessToken";

    when(organizationApisHolderMock.getOrgSubUnitEntityExtendedControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityExtendedControllerApiControllerApiMock);

    orgSubUnitEntityExtendedClient.updateStatus(organizationId, subUnitCode, orgSubUnitStatus, accessToken);

    verify(orgSubUnitEntityExtendedControllerApiControllerApiMock).updateStatus(organizationId, subUnitCode, orgSubUnitStatus);
  }
}
