package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityExtendedClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitSearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final String accessToken = "accessToken";

  @Mock
  private OrgSubUnitEntityClient orgSubUnitEntityClientMock;
  @Mock
  private OrgSubUnitEntityExtendedClient orgSubUnitEntityExtendedClientMock;
  @Mock
  private OrgSubUnitSearchClient orgSubUnitSearchClientMock;

  private OrgSubUnitService service;

  @BeforeEach
  void setUp() {
    service = new OrgSubUnitServiceImpl(
      orgSubUnitEntityClientMock,
      orgSubUnitEntityExtendedClientMock,
      orgSubUnitSearchClientMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      orgSubUnitEntityClientMock,
      orgSubUnitEntityExtendedClientMock,
      orgSubUnitSearchClientMock
    );
  }

  @Test
  void whenGetOrgSubUnitByIdThenInvokeClient() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitEntityClientMock.getOrgSubUnitById(orgSubUnitId, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = service.getOrgSubUnitById(orgSubUnitId, accessToken);

    // Then
    assertSame(expectedResult, result);
  }

  @Test
  void whenCreateOrgSubUnitThenInvokeClient() {
    // Given
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitEntityClientMock.createOrgSubUnit(requestBody, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = service.createOrgSubUnit(requestBody, accessToken);

    // Then
    assertSame(expectedResult, result);
  }

  @Test
  void whenDeleteOrgSubUnitThenInvokeClient() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";

    // When & Then
    assertDoesNotThrow(() -> service.deleteOrgSubUnit(orgSubUnitId, accessToken));
    verify(orgSubUnitEntityClientMock).deleteOrgSubUnit(orgSubUnitId, accessToken);
  }

  @Test
  void whenUpdateOrgSubUnitThenInvokeClient() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitEntityClientMock.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = service.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken);

    // Then
    assertSame(expectedResult, result);
  }

  @Test
  void whenUpdateOrgSubUnitStatusThenInvokeClient() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus orgSubUnitStatus = OrgSubUnitStatus.CANCELLED;

    service.updateOrgSubUnitStatus(organizationId, subUnitCode, orgSubUnitStatus, accessToken);

    verify(orgSubUnitEntityExtendedClientMock).updateStatus(organizationId, subUnitCode, orgSubUnitStatus, accessToken);
  }

  @Test
  void whenFindByOrganizationIdAndFiltersThenInvokeClient() {
    Long organizationId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus status = OrgSubUnitStatus.ACTIVE;
    SubUnitType subUnitType = SubUnitType.AOO;
    Pageable pageable = PageRequest.ofSize(10);

    PagedModelOrgSubUnit expectedResult = podamFactory.manufacturePojo(PagedModelOrgSubUnit.class);

    when(orgSubUnitSearchClientMock.findByOrganizationIdAndFilters(
      organizationId,
      operatorExternalUserId,
      subUnitCode,
      status,
      subUnitType,
      pageable,
      accessToken
    )).thenReturn(expectedResult);

    PagedModelOrgSubUnit result = service.findByOrganizationIdAndFilters(
      organizationId,
      operatorExternalUserId,
      subUnitCode,
      status,
      subUnitType,
      pageable,
      accessToken
    );

    assertSame(expectedResult, result);
  }
}
