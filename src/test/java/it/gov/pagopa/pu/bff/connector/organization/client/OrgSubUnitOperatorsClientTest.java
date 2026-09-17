package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrgSubUnitOperatorsApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsClientTest {

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;

  @Mock
  private OrgSubUnitOperatorsApi orgSubUnitOperatorsApiMock;

  private OrgSubUnitOperatorsClient client;

  @BeforeEach
  void setUp() {
    client = new OrgSubUnitOperatorsClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      orgSubUnitOperatorsApiMock
    );
  }

  @Test
  void whenAddOrgSubUnitsToOperatorThenInvokeApi() {
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";
    List<String> orgSubUnitCodes = List.of("SUB_UNIT_1", "SUB_UNIT_2");
    String accessToken = "accessToken";

    when(organizationApisHolderMock.getOrgSubUnitOperatorsApi(accessToken))
      .thenReturn(orgSubUnitOperatorsApiMock);

    client.addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes, accessToken);

    verify(organizationApisHolderMock).getOrgSubUnitOperatorsApi(accessToken);
    verify(orgSubUnitOperatorsApiMock).addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes);
  }

  @Test
  void whenDeleteOrgSubUnitFromOperatorThenInvokeApi() {
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";
    String subUnitCode = "SUB_UNIT_1";
    String accessToken = "accessToken";

    when(organizationApisHolderMock.getOrgSubUnitOperatorsApi(accessToken))
      .thenReturn(orgSubUnitOperatorsApiMock);

    client.deleteOrgSubUnitFromOperator(organizationId, mappedExternalUserId, subUnitCode, accessToken);

    verify(organizationApisHolderMock).getOrgSubUnitOperatorsApi(accessToken);
    verify(orgSubUnitOperatorsApiMock).deleteOrgSubUnitFromOperator(organizationId, mappedExternalUserId, subUnitCode);
  }
}
