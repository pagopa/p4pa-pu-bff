package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrgSubUnitApi;
import it.gov.pagopa.pu.organization.dto.generated.OrgAndSubUnitDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitClientTest {

  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private OrgSubUnitApi orgSubUnitApiMock;

  @InjectMocks
  private OrgSubUnitClient client;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder
    );
  }

  @Test
  void whenGetOrgSubUnitByIdThenInvokeWithAccessToken() {
    // Given
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    List<OrgAndSubUnitDTO> expectedResult = List.of(new OrgAndSubUnitDTO());

    when(organizationApisHolder.getOrgSubUnitApi(accessToken))
      .thenReturn(orgSubUnitApiMock);
    when(orgSubUnitApiMock.getOrgSubUnitWithNoServiceType(organizationId, PdndServiceType.SEND))
      .thenReturn(expectedResult);

    // When
    List<OrgAndSubUnitDTO> result = client.getOrgSubUnitWithNoServiceType(organizationId, PdndServiceType.SEND, accessToken);

    // Then
    assertSame(expectedResult, result);
  }


}
